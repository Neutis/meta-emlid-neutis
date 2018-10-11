# Check a file system.
# $1=device
checkfs_once() {
  DEV="$1"
  FSCKCODE=0

  if [ "$fastboot" = "y" ]; then
    log_warning_msg "Fast boot enabled, so skipping $DEV check."
    return
  fi

  if ! command -v e2fsck >/dev/null 2>&1; then
    log_warning_msg "e2fsck not present, so skipping $DEV"
    return
  fi

  if [ "$forcefsck" = "y" ]; then
    force="-f"
  else
    force=""
  fi

  e2fsck -y $force $DEV
  FSCKCODE=$?

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
  local DEV="$1"

  if [[ -b $DEV ]]; then
    while ! checkfs_once "$@"; do
	  panic "The $2 filesystem on $1 requires a manual fsck"
    done
  fi
}
