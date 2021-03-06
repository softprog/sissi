package com.sissi.protocol.iq.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sissi.ucenter.field.Field;
import com.sissi.ucenter.field.Fields;

/**
 * @author kim 2013年12月4日
 */
@XmlRootElement(name = XInput.NAME)
public class XInput implements Field<XValue> {

	public final static String NAME = "field";

	private String var;

	private String type;

	private String name;

	private XValue value;

	private XRequired required;

	public XInput() {

	}

	public XInput(String type, String name, String var) {
		this.type = XFieldType.parse(type).toString();
		this.name = name;
		this.var = var;
	}

	public XInput(String type, String name, String var, XRequired required) {
		this(type, name, var);
		this.required = required;
	}

	public XInput(String type, String name, String var, String value) {
		this(type, name, var);
		this.value = new XValue(value);
	}

	public XInput(String type, String name, String var, String value, XRequired required) {
		this(type, name, var, required);
		this.value = new XValue(value);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@XmlAttribute(name = "label")
	public String getLabel() {
		return this.name;
	}

	@XmlAttribute
	public String getVar() {
		return this.var;
	}

	@XmlAttribute
	public String getType() {
		return this.type;
	}

	@Override
	@XmlElement(name = XValue.NAME)
	public XValue getValue() {
		return this.value;
	}

	@XmlElement
	public XRequired getRequired() {
		return this.required;
	}

	@Override
	public Fields getChildren() {
		return null;
	}

	@Override
	public boolean hasChild() {
		return false;
	}
}