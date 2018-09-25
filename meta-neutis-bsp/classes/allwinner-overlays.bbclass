deploy_allwinner_device_tree_blobs() {
	if [ ${SOC_FAMILY} = "sun50i" ]; then
		mkdir -p ${DEPLOY_DIR_IMAGE}/${MANUFACTURER}
		rm -rf ${DEPLOY_DIR_IMAGE}/${MANUFACTURER}/*
		mkdir -p ${DEPLOY_DIR_IMAGE}/${MANUFACTURER}/overlay
	else
		bbwarn "No one expects it to happen: ${SOC_FAMILY}"
	fi

	if test -n "${KERNEL_DEVICETREE}"; then
		for DTS_FILE in ${KERNEL_DEVICETREE}; do
			DTS_BASE_NAME=`basename ${DTS_FILE} | awk -F "." '{print $1}'`
			DTS_BASE_EXT=`basename ${DTS_FILE} | awk -F "." '{print $2}'`
			if [ -e ${DEPLOY_DIR_IMAGE}/"${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.${DTS_BASE_EXT}" ]; then
				kernel_bin="`readlink ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${MACHINE}.bin`"
				kernel_bin_for_dtb="`readlink ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.${DTS_BASE_EXT} | sed "s,$DTS_BASE_NAME,${MACHINE},g;s,\.${DTS_BASE_EXT}$,.bin,g"`"
				if [ $kernel_bin = $kernel_bin_for_dtb ]; then
					if [ ${SOC_FAMILY} = "sun50i" ]; then
						if [ ${DTS_BASE_EXT} = "dtbo" ]; then
							cp ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.${DTS_BASE_EXT} ${DEPLOY_DIR_IMAGE}/${MANUFACTURER}/overlay/${DTS_BASE_NAME}.${DTS_BASE_EXT}
						else
							cp ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.${DTS_BASE_EXT} ${DEPLOY_DIR_IMAGE}/${MANUFACTURER}/${DTS_BASE_NAME}.${DTS_BASE_EXT}
						fi
					else
						mcopy -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.${DTS_BASE_EXT} ::/${DTS_BASE_NAME}.${DTS_BASE_EXT}
					fi
				fi
			fi
		done
	else
		bbwarn "KERNEL_DEVICETREE shouldn't be empty by this time"
	fi
}
