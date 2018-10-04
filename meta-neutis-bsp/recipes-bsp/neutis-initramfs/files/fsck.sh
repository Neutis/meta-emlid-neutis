# Check a file system.
# $1=device
checkfs_once() {
  DEV="$1"

  TYPE=$(get_fstype "$1")

  FSCKCODE=0
 
  if ! command -v fsck >/dev/null 2>&1; then
    log_warning_msg "fsck not present, so skipping $DEV"
    return
  fi
	
  if [ "$fastboot" = "y" ]; then
    log_warning_msg "Fast boot enabled, so skipping $DEV check."
    return
  fi

  if [ "$forcefsck" = "y" ]; then
    force="-f"
  else
    force=""
  fi

  if [ "$fsckfix" = "y" ]; then
    fix="-y"
  elif [ "$fsckfix" = "n" ]; then
    fix="-n"
  else
    fix="-a"
  fi

  spinner=""
  if [ -z "${debug}" ]; then
    spinner="-C"
  fi

  if [ "${quiet}" = n ]; then
    log_begin_msg "Will now check $DEV file system"
    fsck $force $fix -V -t $TYPE $DEV
    FSCKCODE=$?
    log_end_msg
  else
    log_begin_msg "Checking $DEV file system"
    fsck $force $fix -T -t $TYPE $DEV
    FSCKCODE=$?
    log_end_msg
  fi

  # NOTE: "failure" is defined as exiting with a return code of
  # 4, possibly or-ed with other flags. A return code of 1
  # indicates that file system errors were corrected but that
  # the boot may proceed.
  #
  if [ "$FSCKCODE" -eq 32 ]; then
    log_warning_msg "File system check was interrupted by user"
  elif [ $((FSCKCODE & 4)) -eq 4 ]; then
    log_failure_msg "File system check of the $DEV filesystem failed"
    return 1
  elif [ "$FSCKCODE" -gt 1 ]; then
    log_warning_msg "File system check failed but did not detect errors"
    sleep 5
  else
    echo $FSCKCODE
  fi
    return 0
}

checkfs() {
  ln -s /sbin/fsck.ext4 /bin/fsck.ext4
  ln -s /sbin/fsck.ext2 /bin/fsck.ext2
  ln -s /sbin/fsck.fat /bin/fsck.fat
  local DEV="$1"

  if [[ -b $DEV ]]; then
    while ! checkfs_once "$@"; do
	  panic "The $2 filesystem on $1 requires a manual fsck"
    done
  fi
}

# Parameter: device node to check
# Echos fstype to stdout
# Return value: indicates if an fs could be recognized
get_fstype () {
  local FS FSTYPE FSSIZE RET
  FS="${1}"

  if command -v blkid >/dev/null 2>&1; then
    FSTYPE=$(blkid -o value -s TYPE "${FS}")
  elif [ -x /lib/udev/vol_id ]; then
    FSTYPE=$(/lib/udev/vol_id -t "${FS}" 2> /dev/null)
  fi
  RET=$?

  if [ -z "${FSTYPE}" ]; then
    FSTYPE="unknown"
  fi

  echo "${FSTYPE}"
  return ${RET}
}
