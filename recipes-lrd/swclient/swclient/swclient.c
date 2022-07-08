/*
 * (C) Copyright 2019
 *
 * SPDX-License-Identifier:     GPL-2.0-or-later
 */

/*
 * The library wraps swupdate client APIs
 */

#include <progress_ipc.h>
#include <network_ipc.h>

#include <Python.h>

static PyObject * prepare_fw_update(PyObject *self, PyObject *args)
{
	struct swupdate_request req;
	const char *software_set;
	const char *running_mode;
	int dryrun = 1, fd = -1;

	if (!PyArg_ParseTuple(args, "i|ss", &dryrun, &software_set, &running_mode)) {
		PyErr_SetString(PyExc_RuntimeError, "prepare_fw_update: PyArg_ParseTuple failed");
		return NULL;
	}

	swupdate_prepare_req(&req);
	req.dry_run = dryrun ? RUN_DRYRUN : RUN_INSTALL;
	if (software_set && *software_set) {
		strncpy(req.software_set, software_set, sizeof(req.software_set));
		req.software_set[sizeof(req.software_set) - 1] = '\0';
	}
	if (running_mode && *running_mode) {
		strncpy(req.running_mode, running_mode, sizeof(req.running_mode));
		req.running_mode[sizeof(req.running_mode) - 1] = '\0';
	}

	fd = ipc_inst_start_ext(&req, sizeof(req));

	return Py_BuildValue("i", fd);
}

static PyObject * do_fw_update(PyObject *self, PyObject *args)
{
	int rc, fd = -1;
	Py_buffer py_buf;

	if (!PyArg_ParseTuple(args, "y*i", &py_buf, &fd)) {
		PyErr_SetString(PyExc_RuntimeError, "do_fw_update: PyArg_ParseTuple failed");
		return NULL;
	}

	if (fd < 0) {
		PyErr_SetString(PyExc_RuntimeError, "do_fw_update: invalid file descriptor");
		return NULL;
	}

	rc = ipc_send_data(fd, py_buf.buf, py_buf.len);

	PyBuffer_Release(&py_buf);

	return Py_BuildValue("i", rc);
}

static PyObject * end_fw_update(PyObject *self, PyObject *args)
{
	int fd = -1;

	if (!PyArg_ParseTuple(args, "i", &fd)) {
		PyErr_SetString(PyExc_RuntimeError, "end_fw_update: PyArg_ParseTuple failed");
		return NULL;
	}

	ipc_end(fd);

	Py_RETURN_NONE;
}

static PyObject * open_progress_ipc(PyObject *self, PyObject *Py_UNUSED(ignored))
{
	/* Open IPC channel file descriptor */
	int msg_fd = progress_ipc_connect(false);

	return Py_BuildValue("i", msg_fd);
}

static PyObject * read_progress_ipc(PyObject *self, PyObject *args)
{
	struct progress_msg msg;
	int rc, msg_fd = -1;

	if (!PyArg_ParseTuple(args, "i", &msg_fd)) {
		PyErr_SetString(PyExc_RuntimeError, "read_progress_ipc: PyArg_ParseTuple failed");
		return NULL;
	}

	/* Ensure the provided file descriptor is valid */
	if (msg_fd < 0) {
		PyErr_SetString(PyExc_RuntimeError, "read_progress_ipc: invalid file descriptor");
		return NULL;
	}

	/* Read from the IPC channel */
	rc = progress_ipc_receive(&msg_fd, &msg);
	if (rc <= 0) {
		PyErr_SetString(PyExc_RuntimeError, "read_progress_ipc: could not read from IPC channel");
		return NULL;
	}

	/* Return the message result to the caller */
	return Py_BuildValue("iIIIss", msg.status, msg.nsteps, msg.cur_step,
			     msg.cur_percent, msg.cur_image, msg.info);
}

static PyObject * close_progress_ipc(PyObject *self, PyObject *args)
{
	int msg_fd = -1;

	if (!PyArg_ParseTuple(args, "i", &msg_fd)) {
		PyErr_SetString(PyExc_RuntimeError, "close_progress_ipc: PyArg_ParseTuple failed");
		return NULL;
	}

	/* Ensure the provided file descriptor is valid */
	if (msg_fd < 0) {
		PyErr_SetString(PyExc_RuntimeError, "close_progress_ipc: invalid file descriptor");
		return NULL;
	}

	/* Close the IPC channel connection */
	ipc_end(msg_fd);

	Py_RETURN_NONE;
}

static PyMethodDef swclient_methods[] =
{
	{ "prepare_fw_update",	prepare_fw_update,  METH_VARARGS, "Prepare to update firmware"	     },
	{ "do_fw_update",	do_fw_update,	    METH_VARARGS, "Do firmware update"		     },
	{ "end_fw_update",	end_fw_update,	    METH_VARARGS, "End firmware update"		     },
	{ "open_progress_ipc",	open_progress_ipc,  METH_NOARGS,  "Open progress IPC connection"     },
	{ "read_progress_ipc",	read_progress_ipc,  METH_VARARGS, "Read progress via IPC connection" },
	{ "close_progress_ipc", close_progress_ipc, METH_VARARGS, "Close progress IPC connection"    },
	{ NULL,			NULL,		    0,		  NULL				     }
};

// The arguments of this structure tell Python what to call your extension,
// what it's methods are and where to look for it's method definitions
static struct PyModuleDef swclient_definition =
{
	PyModuleDef_HEAD_INIT,
	"swclient",
	"A Python module that wraps swupdate client APIs.",
	-1,
	swclient_methods
};

PyMODINIT_FUNC PyInit_swclient(void)
{
	return PyModule_Create(&swclient_definition);
}
