package org.github.grails.taglib.bootstrap

class Attribute {

	private final static String QUOTE = "\"";
	private final static String SPACE = " ";
	private final static String EQUALS = "=";
	
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
	
	public static void main(String... args) {
		def attrs = [fluid: 'true']
		println Boolean.valueOf(attrs.remove('fluid'))
	}
}
