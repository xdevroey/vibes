<?xml version="1.0"?><ts>
    <start>state1</start>
    <states>
        <state id="state9">
            <transition target="state1" action="close"></transition>
        </state>
        <state id="state6">
            <transition target="state7" action="serveTea"></transition>
        </state>
        <state id="state5">
            <transition target="state7" action="serveSoda"></transition>
        </state>
        <state id="state8">
            <transition target="state9" action="take"></transition>
            <transition target="state2" action="serveTea"></transition>
        </state>
        <state id="state7">
            <transition target="state8" action="open"></transition>
            <transition target="state1" action="take"></transition>
        </state>
        <state id="state2">
            <transition target="state3" action="change"></transition>
        </state>
        <state id="state1">
            <transition target="state2" action="pay"></transition>
            <transition target="state3" action="free"></transition>
        </state>
        <state id="state4">
            <transition target="state1" action="return"></transition>
        </state>
        <state id="state3">
            <transition target="state4" action="cancel"></transition>
            <transition target="state6" action="tea"></transition>
            <transition target="state5" action="soda"></transition>
        </state>
    </states>
</ts>