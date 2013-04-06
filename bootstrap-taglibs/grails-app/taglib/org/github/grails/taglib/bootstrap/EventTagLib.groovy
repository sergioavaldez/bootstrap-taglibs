package org.github.grails.taglib.bootstrap

import grails.converters.JSON;

class EventTagLib extends BaseTagLib {

	static namespace = "bs"
	
	private static final String ON_EVENT_STARTED = "bsOnEventStarted"
	
	/**
	 * Will create a javaScript event at end of the page like:
	 * 
	 * <code>
	 * $('<componentId>').on('<event>', function() {
	 * 		<scriptBody>
	 * });
	 * </code>
	 * 
	 * @attr event REQUIRED Event name, depends of the component events.
	 * @attr componentId Is not required if this tag is declared into
	 * any component.
	 */
	def on = {attrs, body->
		pageScope."$ON_EVENT_STARTED" = true
		
		def scopeComponentId = attrs.remove('componentId') ?: pageScope."$CURRENT_COMPONENT_ID"
		
		g.javascript() {
			"""
			\$('#$scopeComponentId').on('${attrs.event}', function(){
				${body()}
			});
			"""
		}
		
		pageScope."$ON_EVENT_STARTED" = false
	}
	
	/**
	 * Creates component options via javaScript.<br />
	 * 
	 * Example:<br />
	 * Out of component:
	 * <code>
	 * <bs:componentOptions componentId="compId" type="modal" option1="value1" option2="value2"/>
	 * </code>
	 * <br />
	 * Generate:<br />
	 * <code>
	 * $('#compId').modal({"option1": "value1", "option2": "value2"});
	 * </code>
	 * <br />
	 * Into a component:<br />
	 * <code>
	 * <bs:modal id="myModal">
	 * 		<!-- Modal header, body and footer -->
	 * 		<b><bs:componentOptions backdrop="false" show="false"/></b>
	 * </bs:modal>
	 * </code>
	 * Generate:<br />
	 * <code>
	 * $('#myModal').modal({"backdrop": false, "show": false});
	 * </code>
	 * @emptyTag
	 * 
	 */
	def componentOptions = {attrs->
		def componentType = attrs.remove('type') ?: pageScope."$CURRENT_COMPONENT_TYPE"
		def scopeComponentId = attrs.remove('componentId') ?: pageScope."$CURRENT_COMPONENT_ID"
		
		g.javascript() {
			"""
			\$('#$scopeComponentId').$componentType(${attrs as JSON});
			"""
		}
	}
	
	/**
	 * Creates component options via javaScript.<br />
	 *
	 * Example:<br />
	 * Out of component:
	 * <code>
	 * <bs:componentOptions componentId="compId" type="modal" option1="value1" option2="value2"/>
	 * </code>
	 * <br />
	 * Generate:<br />
	 * <code>
	 * $('#compId').modal({"option1": "value1", "option2": "value2"});
	 * </code>
	 * <br />
	 * Into a component:<br />
	 * <code>
	 * <bs:modal id="myModal">
	 * 		<!-- Modal header, body and footer -->
	 * 		<b><bs:componentOptions backdrop="false" show="false"/></b>
	 * </bs:modal>
	 * </code>
	 * Generate:<br />
	 * <code>
	 * $('#myModal').modal({"backdrop": false, "show": false});
	 * </code>
	 * @emptyTag
	 *
	 */
	def componentMethod = {attrs->
		def intoOnComponent 	= Boolean.valueOf(pageScope."$ON_EVENT_STARTED")
		def avoidInheritance 	= Boolean.valueOf(attrs.remove('avoidInheritance'))
		def scopeComponentId 	= attrs.remove('componentId') ?: pageScope."$CURRENT_COMPONENT_ID"
		def componentType 		= attrs.remove('componentType') ?: pageScope."$CURRENT_COMPONENT_TYPE"
		
		
		scopeComponentId = intoOnComponent ? 'this' : "#$scopeComponentId"
		
		g.javascript() {
			"""
			\$('$scopeComponentId').$componentType(${attrs as JSON});
			"""
		}
	}
	
}
