<?xml version="1.0"?>
<ts>
	<start>s6</start>
	<states>
		<state id="s6">
			<transition target="s7" action="receiveMsg"/>
		</state>
		<state id="s7">
			<transition target="s8"  action="commandMsg"/>
			<transition target="s16"  action="palarmMsg"/>
			<transition target="s20"  action="levelMsg"/>
		</state>
		<state id="s8">
			<transition target="s9"  action="stopCmd"/>
			<transition target="s13"  action="startCmd"/>
		</state>
		<state id="s9">
			<transition target="s10" action="isRunning"/>
			<transition target="s11" action="isNotRunning"/>
		</state>
		<state id="s10">
			<transition target="s11" action="pumpStop"/>
		</state>
		<state id="s11">
			<transition target="s12" action="setStop"/>
		</state>
		<state id="s12">
			<transition target="s6" action="end"/>
		</state>
		<state id="s13">
			<transition target="s14" action="isNotRunning"/>
			<transition target="s15" action="isRunning"/>
			<transition target="s15" action="isReady"/>
		</state>
		<state id="s14">
			<transition target="s15" action="setReady"/>
		</state>
		<state id="s15">
			<transition target="s6" action="end"/>
		</state>
		<state id="s16">
			<transition target="s17" action="isRunning"/>
			<transition target="s18" action="isNotRunning"/>
		</state>
		<state id="s17">
			<transition target="s18" action="pumpStop"/>
		</state>
		<state id="s18">
			<transition target="s19" action="setMethaneStop"/>
		</state>
		<state id="s19">
			<transition target="s6" action="end"/>
		</state>
		<state id="s20">
			<transition target="s6"  action="end"/>
			<transition target="s21"  action="highLevel"/>
			<transition target="s27"  action="lowLevel"/>
		</state>
		<state id="s21">
			<transition target="s22"  action="isReady"/>
			<transition target="s22"  action="isLowStop"/>
			<transition target="s26" action="isRunning"/>
			<transition target="s26" action="isStopped"/>
			<transition target="s26"  action="isMethaneStop"/>
		</state>
		<state id="s22">
			<transition target="s23" action="setReady"/>
			<transition target="s23"  action="setMethaneStop"/>
		</state>
		<state id="s23">
			<transition target="s24" action="isReady"/>
			<transition target="s26" action="isNotReady"/>
		</state>
		<state id="s24">
			<transition target="s25" action="pumpStart"/>
		</state>
		<state id="s25">
			<transition target="s26" action="setRunning"/>
		</state>
		<state id="s26">
			<transition target="s6"  action="end"/>
		</state>
		<state id="s27">
			<transition target="s28" action="isRunning"/>
			<transition target="s30" action="isNotRunning"/>
		</state>
		<state id="s28">
			<transition target="s29" action="pumpStop"/>
		</state>
		<state id="s29">
			<transition target="s30" action="setLowStop"/>
		</state>
		<state id="s30">
			<transition target="s6" action="end"/>
		</state>
	</states>			
</ts>
