#
# Copyright (C) 2011 The Android Open-Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Flags
TARGET_GLOBAL_CFLAGS += -mfpu=neon -mfloat-abi=softfp
TARGET_GLOBAL_CPPFLAGS += -mfpu=neon -mfloat-abi=softfp

TARGET_SPECIFIC_HEADER_PATH := device/zte/grand_memo/include

# QCOM hardware
BOARD_USES_QCOM_HARDWARE := true
TARGET_USES_QCOM_BSP     := true
COMMON_GLOBAL_CFLAGS     += -DQCOM_HARDWARE -DQCOM_BSP

# Architecture
TARGET_CPU_ABI := armeabi-v7a
TARGET_CPU_ABI2 := armeabi
TARGET_CPU_SMP := true
TARGET_CPU_VARIANT := krait
TARGET_ARCH := arm
TARGET_ARCH_VARIANT := armv7-a-neon
ARCH_ARM_HAVE_TLS_REGISTER := true
TARGET_BOARD_PLATFORM    := msm8960

# Krait optimizations
TARGET_USE_KRAIT_BIONIC_OPTIMIZATION := true
TARGET_USE_KRAIT_PLD_SET             := true
TARGET_KRAIT_BIONIC_PLDOFFS          := 10
TARGET_KRAIT_BIONIC_PLDTHRESH        := 10
TARGET_KRAIT_BIONIC_BBTHRESH         := 64
TARGET_KRAIT_BIONIC_PLDSIZE          := 64

# Others
TARGET_NO_RADIOIMAGE       := true
BOARD_USES_SECURE_SERVICES := true
BOARD_EGL_CFG              := device/zte/grand_memo/configs/egl.cfg

# Filesystem
BOARD_HAS_LARGE_FILESYSTEM         := true
TARGET_USERIMAGES_USE_EXT4         := true
BOARD_BOOTIMAGE_PARTITION_SIZE     := 15728640 # 15M
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 15728640 # 15M
BOARD_SYSTEMIMAGE_PARTITION_SIZE   := 2147483648 # 2G
BOARD_USERDATAIMAGE_PARTITION_SIZE := 2859941888 # 2.66G
BOARD_FLASH_BLOCK_SIZE             := 131072

# Caf
TARGET_QCOM_MEDIA_VARIANT   := caf
TARGET_QCOM_DISPLAY_VARIANT := caf
TARGET_QCOM_AUDIO_VARIANT   := caf

# QCOM enhanced A/V
TARGET_ENABLE_QC_AV_ENHANCEMENTS := true

# GPS
BOARD_HAVE_NEW_QC_GPS := true

# Enable QC time
BOARD_USES_QC_TIME_SERVICES := true

# Audio
BOARD_USES_ALSA_AUDIO              := true
BOARD_HAVE_NEW_QCOM_CSDCLIENT      := true
BOARD_USES_FLUENCE_INCALL          := true
BOARD_USES_SEPERATED_VOIP          := true
BOARD_USES_SEPERATED_VOICE_SPEAKER := true
TARGET_USES_QCOM_COMPRESSED_AUDIO  := true

# Display
TARGET_USES_ION             := true
USE_OPENGL_RENDERER         := true
TARGET_USES_C2D_COMPOSITION := true
NUM_FRAMEBUFFER_SURFACE_BUFFERS := 3

# Compatibility with pre-kitkat Qualcomm sensor HALs
SENSORS_NEED_SETRATE_ON_ENABLE := true

# Camera
COMMON_GLOBAL_CFLAGS += -DDISABLE_HW_ID_MATCH_CHECK -DQCOM_BSP_CAMERA_ABI_HACK -DNEEDS_VECTORIMPL_SYMBOLS

# Webkit
ENABLE_WEBGL            := true
TARGET_FORCE_CPU_UPLOAD := true

# FIXME: HOSTAPD-derived wifi driver
BOARD_HAS_QCOM_WLAN              := true
BOARD_WLAN_DEVICE                := qcwcn
WPA_SUPPLICANT_VERSION           := VER_0_8_X
BOARD_WPA_SUPPLICANT_DRIVER      := NL80211
BOARD_WPA_SUPPLICANT_PRIVATE_LIB := lib_driver_cmd_$(BOARD_WLAN_DEVICE)
BOARD_HOSTAPD_DRIVER             := NL80211
BOARD_HOSTAPD_PRIVATE_LIB        := lib_driver_cmd_$(BOARD_WLAN_DEVICE)
WIFI_DRIVER_FW_PATH_STA          := "sta"
WIFI_DRIVER_FW_PATH_AP           := "ap"

# Bluetooth
BOARD_HAVE_BLUETOOTH                        := true
BOARD_HAVE_BLUETOOTH_QCOM                   := true
BLUETOOTH_HCI_USE_MCT                       := true

TARGET_NO_RECOVERY := true

# Don't build qcom camera HAL
USE_CAMERA_STUB                      := false
USE_DEVICE_SPECIFIC_CAMERA           := true
USE_DEVICE_SPECIFIC_QCOM_PROPRIETARY := true

HAVE_ADRENO_SOURCE:= false

# Board specific SELinux policy variable definitions
BOARD_SEPOLICY_DIRS := \
       device/zte/grand_memo/sepolicy

BOARD_SEPOLICY_UNION := \
       app.te \
       bluetooth.te \
       device.te \
       domain.te \
       drmserver.te \
       file.te \
       file_contexts \
       hci_init.te \
       init_shell.te \
       keystore.te \
       mediaserver.te \
       kickstart.te \
       nfc.te \
       rild.te \
       surfaceflinger.te \
       system.te \
       ueventd.te \
       wpa.te

#Vold
TARGET_USE_CUSTOM_LUN_FILE_PATH := "/sys/devices/platform/msm_hsusb/gadget/lun%d/file"

#RIL
#BOARD_RIL_CLASS := ../../../device/zte/grand_memo/ril/
