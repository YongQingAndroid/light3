package com.lightpermission.permission.requestresult;


import com.lightpermission.permission.State;

public class PermissionResult {
    State state;

    PermissionResult(String[] allow, String[] disAllow) {
        if (disAllow == null || disAllow.length == 0) {
            state = State.DONE;
        } else {
            state = State.FAIL;
        }
        state.setFail(disAllow);
        state.setPermission(allow);
    }
}
