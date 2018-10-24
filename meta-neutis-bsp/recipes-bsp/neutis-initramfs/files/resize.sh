ln -s /sbin/resize2fs /bin/resize2fs

# $1=device
resize_partition() {

  if [ -z ${1} ] || [ ! -b ${1} ]; then
    log_warning_msg "block device not present, so skipping resizing partition"
    return 1
  fi

  local blkdev partnr currpartsize resizeopts resize2fsopts

  blkdev=${1%p*}
  partnr=${1#${blkdev}p}

  if [ -b "${blkdev}" -a ! -z "${partnr}" ]; then
    currpartsize=$(parted -s ${blkdev} unit % print 2>/dev/null | grep '^[ \n]'${partnr} | awk '{print $3}' | grep -o '^[^.A-Z%]*')
  else
    log_warning_msg "block device is not present or partition number is empty"
    return 1
  fi

  if [ ! ${currpartsize} ]; then
    log_warning_msg "unable to get size of ${1}"
    return 1
  fi

  if [ ${currpartsize} -ge 100 ]; then
    resize2fsopts="> /dev/null 2>&1"
  elif [ ${currpartsize} -lt 100 ];then
    resizeopts="resizepart ${partnr} 100"
    parted -s ${blkdev} unit % ${resizeopts} print
  else
    log_warning_msg "unable to get valid size of ${1}"
    return 1
  fi

  eval resize2fs -f ${1} ${resize2fsopts}
}

