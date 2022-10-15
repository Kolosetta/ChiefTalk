package com.example.chieftalk.utilits

enum class AppStates(val state: String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает");

    companion object{
        fun updateState(appState: AppStates){
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_STATE)
                .setValue(appState.state)
                .addOnSuccessListener {
                    USER.state = appState.state
                }
        }
    }
}