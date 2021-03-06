<?xml version="1.0" encoding="UTF-8"?>
<ts:ts xmlns:ts="http://www.unamur.be/xml/ts/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<ts:start>state1</ts:start>
	<ts:states>
		<ts:state id="state2">
			<ts:transition action="change" target="state3" />
		</ts:state>
		<ts:state id="state3">
			<ts:transition action="cancel" target="state4" />
			<ts:transition action="tea" target="state6" />
			<ts:transition action="soda" target="state5" />
		</ts:state>
		<ts:state id="state4">
			<ts:transition action="return" target="state1" />
		</ts:state>
		<ts:state id="state5">
			<ts:transition action="serveSoda" target="state7" />
		</ts:state>
		<ts:state id="state1">
			<ts:transition action="pay" target="state2" />
			<ts:transition action="free" target="state3" />
		</ts:state>
		<ts:state id="state6">
			<ts:transition action="serveTea" target="state7" />
		</ts:state>
		<ts:state id="state7">
			<ts:transition action="open" target="state8" />
			<ts:transition action="take" target="state1" />
		</ts:state>
		<ts:state id="state8">
			<ts:transition action="take" target="state9" />
		</ts:state>
		<ts:state id="state9">
			<ts:transition action="close" target="state1" />
		</ts:state>
	</ts:states>
</ts:ts>
