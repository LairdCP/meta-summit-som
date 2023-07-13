RPMB_FS_DEV_ID ?= "2"

#EXTRA_OEMAKE:append:summit-secure = "\
#	CFG_RPMB_FS=y \
#	CFG_RPMB_FS_DEV_ID=${RPMB_FS_DEV_ID} \
#	CFG_REE_FS=n \
#"

#EXTRA_OEMAKE:append:summit-secure-provision = "\
#	CFG_RPMB_WRITE_KEY=y \
#"
