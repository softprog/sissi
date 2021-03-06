package com.sissi.protocol.muc;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sissi.context.JID;
import com.sissi.protocol.presence.Presence;
import com.sissi.protocol.presence.PresenceType;
import com.sissi.read.Collector;
import com.sissi.read.Metadata;
import com.sissi.ucenter.muc.MucItem;
import com.sissi.ucenter.muc.RelationMuc;

/**
 * @author kim 2014年2月11日
 */
@Metadata(uri = XMucAdmin.XMLNS, localName = Item.NAME)
@XmlType(namespace = XMuc.XMLNS)
@XmlRootElement(name = Item.NAME)
public class Item implements MucItem, Collector {

	private final static Map<String, PresenceType> actions = new HashMap<String, PresenceType>();

	static {
		actions.put(ItemRole.NONE.toString(), PresenceType.UNAVAILABLE);
		actions.put(ItemRole.VISITOR.toString(), PresenceType.AVAILABLE);
		actions.put(ItemRole.MODERATOR.toString(), PresenceType.AVAILABLE);
		actions.put(ItemRole.PARTICIPANT.toString(), PresenceType.AVAILABLE);
	}

	public final static String NAME = "item";

	private String role;

	private String nick;

	private String jid;
	
	private String affiliation;

	private boolean hidden;

	private XReason reason;

	private XActor actor;

	private JID group;

	public Item() {
	}

	public Item(boolean hidden, RelationMuc relation) {
		super();
		this.relation(relation).hidden = hidden;
	}

	public Item relation(RelationMuc relation) {
		this.jid = relation.jid();
		this.role = relation.role();
		this.nick = relation.name();
		this.affiliation = relation.affiliation();
		return this;
	}

	public Item hidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public Presence presence() {
		return new Presence().setType(actions.get(this.getRole())).setFrom(this.group);
	}

	public JID group(JID jid) {
		return this.group = jid.resource(this.getNick());
	}

	@XmlAttribute
	public String getJid() {
		return this.hidden ? null : this.jid;
	}

	@XmlAttribute
	public String getAffiliation() {
		return this.affiliation;
	}

	public Item role(String role) {
		this.role = role;
		return this;
	}

	@XmlAttribute
	public String getRole() {
		return this.role;
	}

	public Item nick(String nick) {
		this.nick = nick;
		return this;
	}

	@XmlAttribute
	public String getNick() {
		return this.nick;
	}

	public String reason() {
		return this.getReason() != null ? this.getReason().getText() : null;
	}

	public Item reason(String reason) {
		if (this.getReason() == null) {
			this.reason = new XReason(reason);
		}
		this.getReason().setText(reason);
		return this;
	}

	@XmlElement
	public XReason getReason() {
		return this.reason;
	}

	public Item actor(JID actor) {
		if (this.actor == null) {
			this.actor = new XActor();
		}
		this.actor.jid(actor);
		return this;
	}

	@XmlElement
	public XActor getActor() {
		return this.hidden ? null : this.actor;
	}

	@Override
	public void set(String localName, Object ob) {
		this.reason = XReason.class.cast(ob);
	}
}
