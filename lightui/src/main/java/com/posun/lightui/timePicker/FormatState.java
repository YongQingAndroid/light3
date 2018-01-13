package com.posun.lightui.timePicker;

public enum FormatState {
        YYYY(-1),
        MM(-1),
        DD(-1),
        HH(-1),
        mm(-1),
        W(-1);
       public int point;

        FormatState(int point) {
            this.point = point;
        }
    }