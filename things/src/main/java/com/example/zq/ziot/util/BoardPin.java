package com.example.zq.ziot.util;

import android.os.Build;

import com.google.android.things.pio.PeripheralManagerService;

import java.util.List;

/**
 * Created by zq on 2017/12/28.
 */

public class BoardPin {

    private static final String EDISON_ARDUINO = "edison_arduino";
    private static final String RASPBERRY = "rpi3";

    public static String getPirPin() {
        switch (getBoardName()) {
        case RASPBERRY:
            return "BCM4";
        case EDISON_ARDUINO:
            return "IO4";
        default:
            throw new IllegalArgumentException("Unsupported device");
         }
    }

    private static String getBoardName() {
        String name = Build.BOARD;
        if (name.equals("edison")) {
            PeripheralManagerService service = new PeripheralManagerService();
            List<String>pinList = service.getGpioList();
            if(pinList.size() > 0) {
                String pinName = pinList.get(0);
                if(pinName.startsWith("IO"))
                     return EDISON_ARDUINO;
            }
        }
        return name;
    }
}