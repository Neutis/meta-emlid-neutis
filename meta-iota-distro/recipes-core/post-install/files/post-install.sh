#!/bin/bash

initialize_ap_mode_cfg () {
    ssid="Iota-id"

    sed -i -e 's/^ssid=.*/ssid='${ssid}'/g' /etc/hostapd.conf
}

exit_post_install () {
    systemctl disable post-install
    sync
}

initialize_ap_mode_cfg

exit_post_install
