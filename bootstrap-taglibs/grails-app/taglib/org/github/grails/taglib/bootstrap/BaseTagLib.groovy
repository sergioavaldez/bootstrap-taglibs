package org.github.grails.taglib.bootstrap

class BaseTagLib {

	static namespace = "bs"
	
	private final static String QUOTE = "\"";
	private final static String SPACE = " ";
	private final static String EQUALS = "=";
	
	protected static final String CURRENT_COMPONENT_ID 	 = "bsCurrentComponentId"
	protected static final String CURRENT_COMPONENT_TYPE = "bsCurrentComponentType"
	
	def resources = {attrs->
		def docsStyle = Boolean.valueOf(attrs.remove('docsStyle'))
		out << "<link rel=\"stylesheet\" href=\"${g.resource(dir: 'css', file: 'bootstrap.min.css')}\" type=\"text/css\">\n"
		if (docsStyle)
			out << "<link rel=\"stylesheet\" href=\"${g.resource(dir: 'css', file: 'bootstrap.docs.css')}\" type=\"text/css\">\n"
		out << g.javascript(src: 'bootstrap.min.js')
	}
	
	/**
	 * Create an id for the current component if it is not created, a assign it in 
	 * page scope in CURRENT_COMPONENT_ID. 
	 * 
	 * @param attrs Component attributes.
	 */
	void createId(def attrs){
		if(!attrs.id) {
			attrs.id = "bsCmntId${new Random().nextInt()}${new Date().getTime()}"
		}
		
		pageScope."$CURRENT_COMPONENT_ID" = attrs.id
	}
	
	void currentComponentType(String type) {
		pageScope."$CURRENT_COMPONENT_TYPE" = type
	}
	
	void cleanCurrentComponent() {
		pageScope."$CURRENT_COMPONENT_ID" 	= null
		pageScope."$CURRENT_COMPONENT_TYPE" = null
	}
	
	/**
	 * Prints parameters to given output. 
	 */
	static void outputAttributes(def attrs, def writer) {
		attrs.remove('tagName') // Just in case one is left
		
		attrs.each { k, v ->
			writer << k
			writer << EQUALS << QUOTE 
			writer << v.encodeAsHTML().trim()
			writer << QUOTE
		}
	}
	
	/**
	 * Method taken from Grails standar Form taglib.	
	 */
	static boolean booleanToAttribute(def attrs, String attrName) {
		def attrValue = attrs.remove(attrName)
		// If the value is the same as the name or if it is a boolean value,
		// reintroduce the attribute to the map according to the w3c rules, so it is output later
		if (Boolean.valueOf(attrValue) ||
		   (attrValue instanceof String && attrValue?.equalsIgnoreCase(attrName))) {
			attrs.put(attrName, attrName)
			return true
		} else if (attrValue instanceof String && !attrValue?.equalsIgnoreCase('false')) {
			// If the value is not the string 'false', then we should just pass it on to
			// keep compatibility with existing code
			attrs.put(attrName, attrValue)
			return false
		}
	}
	
	static void defaultClasses(def attrs, String defaultClass) {
		def pull = attrs.remove('pull')
		def muted = attrs.remove('muted') as boolean
		def clearfix = attrs.remove('clearfix') as boolean
		
		attrs.'class' = "${defaultClass.trim()} ${attrs.'class'?:''}".trim()
		
		if(pull == 'right') {
			addToClass(attrs, 'pull-right')
		} else if(pull == 'left') {
			addToClass(attrs, 'pull-left')
		}
		
		if(muted) {
			addToClass(attrs, "muted")
		}
		
		if(clearfix) {
			addToClass(attrs, "clearfix")
		}
		
		attrs.'class' = attrs.'class'.trim()
	}
	
	static void addToClass (def attrs, String classToAdd) {
		attrs.'class' =  "${(attrs.'class'?:'')} ${classToAdd.trim()}"
		
		attrs.'class' = attrs.'class'.trim()
	}
	
	static void addToDataAPI(def attrs, String property) {
		if(attrs."$property" != null) {
			attrs."data-$property" = attrs.remove(property)
		}
	}
	
}
