/*Written by Aleksandr Aleksandrov <aleksandr.aleksandrov@emlid.com>
*
* Copyright (c) 2018, Emlid Limited
* All rights reserved.
*
* Redistribution and use in source and binary forms,
* with or without modification,
* are permitted provided that the following conditions are met:
*
* 1. Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
*
* 2. Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* 3. Neither the name of the copyright holder nor the names of its contributors
* may be used to endorse or promote products derived from this software
* without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
* THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
* FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
* IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
* BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
* OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
* DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
* AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
* STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
* EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/

#include <stdio.h>
#include <cryptoauthlib.h>

#ifndef SERIAL_APPEND
    #error "SERIAL_APPEND not defined"
#endif

ATCAIfaceCfg g_iface_config = {
    .iface_type        = ATCA_I2C_IFACE,
    .devtype           = ATECC508A,
    .atcai2c           = {
        .slave_address = 0xC0,
        .bus           = 0,
        .baud          = 100000,
    },
    .wake_delay        = 1500,
    .rx_retries        = 20
};

/* 8 bytes must be allocated for serial_number */
int read_atecc_serial_number(char* serial_number)
{
    uint8_t atecc_serial_num[9];
    ATCA_STATUS status;
    int i;

    status = atcab_init(&g_iface_config);

    if (status != ATCA_SUCCESS) {
        printf("atcab_init() failed with ret=0x%08d\r\n", status);
        return -1;
    }

    status = atcab_read_serial_number(atecc_serial_num);
    atcab_release();

    if (status != ATCA_SUCCESS) {
        printf("atcab_read_serial_number() failed with ret=0x%08d\r\n", status);
        return -1;
    }

    sprintf(serial_number, "%s%02X%02X%02X%02X%02X%02X",
            SERIAL_APPEND,
            atecc_serial_num[2],
            atecc_serial_num[3],
            atecc_serial_num[4],
            atecc_serial_num[5],
            atecc_serial_num[6],
            atecc_serial_num[7]);

    return 0;
}

int main(void)
{
    char serial_number[8];

    if (read_atecc_serial_number(serial_number) != 0)
        return -1;

    printf("%s", serial_number);

    return 0;
}
