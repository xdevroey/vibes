/*-
 * #%L
 * VIBeS: dsl
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
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
 * #L%
 */
<?xml version="1.0"?>
<ts>
    <start>s0</start>
    <states>
        <state id="s99">
            <transition target="s0" action="NO_ACTION"/>
        </state>
        <state id="Approach_line_landing_doghouse_and_reference_objects_displayed_end">
            <transition target="symbology_is_displayed_end" action="NO_ACTION"/>
            <transition target="Approach_line_takeoff_doghouse_and_reference_objects_displayed_start" action="Landing_and_touchdown_for_more_than_5_sec"/>
            <transition target="S5" action="NO_ACTION"/>
            <transition target="Pin_with_slope_indication_approach_line_landing_doghouse_displayed" action="Depart_from_landing_position"/>
        </state>
        <state id="landing_position_is_marked_start">
            <transition target="S21" action="NO_ACTION"/>
        </state>
        <state id="Approach_line_takeoff_doghouse_and_real_objects_displayed">
            <transition target="Approach_line_takeoff_doghouse_and_reference_objects_displayed_end" action="NO_ACTION"/>
        </state>
        <state id="Pin_and_approach_line_displayed">
            <transition target="Pin_with_slope_indication_and_approach_line_displayed" action="Approach_to_landing_position"/>
            <transition target="Pin_displayed" action="Depart_from_landing_position"/>
            <transition target="S5" action="Approach_to_landing_position"/>
        </state>
        <state id="Approach_line_landing_doghouse_and_reference_objects_displayed_start">
            <transition target="Approach_line_landing_doghouse_and_virtual_3D_cues_displayed" action="Virtual_3D_cues_displayed"/>
            <transition target="Approach_line_landing_doghouse_and_real_objects_displayed" action="Real_objects_displayed"/>
        </state>
        <state id="symbology_is_displayed_start">
            <transition target="Pin_displayed" action="Approach_to_landing_position"/>
        </state>
        <state id="NO_Ground_displayed">
            <transition target="S21" action="NO_ACTION"/>
        </state>
        <state id="Approach_line_landing_doghouse_and_virtual_3D_cues_displayed">
            <transition target="Approach_line_landing_doghouse_and_reference_objects_displayed_end" action="NO_ACTION"/>
        </state>
        <state id="Pin_displayed">
            <transition target="S5" action="Approach_to_landing_position"/>
            <transition target="Pin_and_approach_line_displayed" action="Approach_to_landing_position"/>
        </state>
        <state id="Landing_Position_is_marked">
            <transition target="NO_Ground_displayed" action="Provide_landing_position_not_on_ground"/>
            <transition target="displayed" action="Provide_landing_position_with_obstacle"/>
            <transition target="landing_position_is_marked_end" action="Provide_valid_landing_position"/>
        </state>
        <state id="Approach_line_landing_doghouse_and_real_objects_displayed">
            <transition target="Approach_line_landing_doghouse_and_reference_objects_displayed_end" action="NO_ACTION"/>
        </state>
        <state id="Pin_with_slope_indication_approach_line_landing_doghouse_displayed">
            <transition target="Approach_line_landing_doghouse_and_reference_objects_displayed_start" action="Approach_to_landing_position"/>
            <transition target="Pin_with_slope_indication_and_approach_line_displayed" action="Depart_from_landing_position"/>
            <transition target="S5" action="NO_ACTION"/>
        </state>
        <state id="s0">
            <transition target="standby" action="activate"/>
        </state>
        <state id="landing_position_is_marked_end">
            <transition target="symbology_is_displayed_start" action="NO_ACTION"/>
        </state>
        <state id="Approach_line_takeoff_doghouse_and_reference_objects_displayed_start">
            <transition target="Approach_line_takeoff_doghouse_and_virtual_3D_cues_displayed" action="Virtual_3D_cues_displayed"/>
            <transition target="Approach_line_takeoff_doghouse_and_real_objects_displayed" action="Real_objects_displayed"/>
        </state>
        <state id="S21">
            <transition target="Landing_Position_is_marked" action="Trigger_mark_landing_position"/>
        </state>
        <state id="Pin_with_slope_indication_and_approach_line_displayed">
            <transition target="Pin_with_slope_indication_approach_line_landing_doghouse_displayed" action="Approach_to_landing_position"/>
            <transition target="S5" action="NO_ACTION"/>
            <transition target="Pin_and_approach_line_displayed" action="Depart_from_landing_position"/>
        </state>
        <state id="S5">
            <transition target="Pin_with_slope_indication_approach_line_landing_doghouse_displayed" action="Depart_from_landing_position"/>
            <transition target="Pin_and_approach_line_displayed" action="Depart_from_landing_position"/>
            <transition target="Pin_displayed" action="Depart_from_landing_position"/>
            <transition target="Pin_with_slope_indication_and_approach_line_displayed" action="Depart_from_landing_position"/>
            <transition target="Approach_line_landing_doghouse_and_reference_objects_displayed_start" action="Approach_to_landing_position"/>
            <transition target="Approach_line_takeoff_doghouse_and_reference_objects_displayed_start" action="Landing_and_touchdown_for_more_than_5_sec"/>
        </state>
        <state id="symbology_is_displayed_end">
            <transition target="s99" action="deactivate"/>
        </state>
        <state id="displayed">
            <transition target="S21" action="NO_ACTION"/>
        </state>
        <state id="standby">
            <transition target="landing_position_is_marked_start" action="NO_ACTION"/>
        </state>
        <state id="Approach_line_takeoff_doghouse_and_reference_objects_displayed_end">
            <transition target="Approach_line_and_takeoff_doghouse_displayed" action="Depart_from_landing_position"/>
            <transition target="S5" action="NO_ACTION"/>
        </state>
        <state id="Approach_line_and_takeoff_doghouse_displayed">
            <transition target="Pin_with_slope_indication_approach_line_landing_doghouse_displayed" action="Depart_from_landing_position"/>
            <transition target="S5" action="NO_ACTION"/>
        </state>
        <state id="Approach_line_takeoff_doghouse_and_virtual_3D_cues_displayed">
            <transition target="Approach_line_takeoff_doghouse_and_reference_objects_displayed_end" action="NO_ACTION"/>
        </state>
    </states>
</ts>
