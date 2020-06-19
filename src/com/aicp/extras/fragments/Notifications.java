/*
 * Copyright (C) 2017-2020 AICP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.aicp.extras.fragments;

import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.aicp.extras.BaseSettingsFragment;
import com.aicp.extras.R;
import com.aicp.extras.utils.Util;
import com.android.internal.util.aicp.DeviceUtils;

public class Notifications extends BaseSettingsFragment {

    private static final String ALERT_SLIDER_PREF = "alert_slider_notifications";
    private static final String PREF_FLASH_ON_CALL = "flashlight_on_call";
    private static final String PREF_FLASH_ON_CALL_DND = "flashlight_on_call_ignore_dnd";
    private static final String PREF_FLASH_ON_CALL_RATE = "flashlight_on_call_rate";

    private SystemSettingIntListPreference mFlashOnCall;
    private SystemSettingSwitchPreference mFlashOnCallIgnoreDND;
    private SystemSettingSeekBarPreference mFlashOnCallRate;


    @Override
    protected int getPreferenceResource() {
        return R.xml.notifications;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util.requireFullStatusbar(getActivity(),
                findPreference(Settings.System.STATUS_BAR_SHOW_TICKER));

        if (!DeviceUtils.deviceSupportsFlashLight(getActivity())) {
            getPreferenceScreen().removePreference(findPreference(
                    Settings.System.FLASHLIGHT_ON_CALL));
        }

        mFlashOnCallRate = (SystemSettingSeekBarPreference)
                findPreference(PREF_FLASH_ON_CALL_RATE);
        int value = Settings.System.getInt(resolver,
                Settings.System.FLASHLIGHT_ON_CALL_RATE, 1);
        mFlashOnCallRate.setValue(value);
        mFlashOnCallRate.setOnPreferenceChangeListener(this);

        mFlashOnCallIgnoreDND = (SystemSettingSwitchPreference)
                findPreference(PREF_FLASH_ON_CALL_DND);
        value = Settings.System.getInt(resolver,
                Settings.System.FLASHLIGHT_ON_CALL, 0);
        mFlashOnCallIgnoreDND.setVisible(value > 1);
        mFlashOnCallRate.setVisible(value != 0);

        mFlashOnCall = (SystemSettingListPreference)
                findPreference(PREF_FLASH_ON_CALL);
        mFlashOnCall.setOnPreferenceChangeListener(this);

        boolean alertSliderAvailable = getActivity().getResources().getBoolean(
                com.android.internal.R.bool.config_hasAlertSlider);
        if (!alertSliderAvailable)
            getPreferenceScreen().removePreference(findPreference(ALERT_SLIDER_PREF));

    }
}
