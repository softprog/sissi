package com.sissi.ucenter.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sissi.context.JID;
import com.sissi.protocol.iq.roster.RosterSubscription;
import com.sissi.protocol.muc.ItemAffiliation;
import com.sissi.protocol.muc.ItemRole;
import com.sissi.ucenter.Relation;
import com.sissi.ucenter.muc.RelationMuc;
import com.sissi.ucenter.roster.RelationRoster;

/**
 * @author kim 2014年2月13日
 */
public class NoneRelation implements Relation, RelationRoster, RelationMuc {

	private static final Map<String, Object> fieldPlus = Collections.unmodifiableMap(new HashMap<String, Object>());

	private static final String zero = "0";

	private final JID jid;

	private final boolean activate;

	private final String subscription;

	private NoneRelation(JID jid, boolean activate, String subscription) {
		super();
		this.jid = jid;
		this.activate = activate;
		this.subscription = subscription;
	}

	public NoneRelation(JID jid, String subscription) {
		this(jid, false, subscription);
	}

	public NoneRelation(JID jid) {
		this(jid, true, zero);
	}

	@Override
	public String getJID() {
		return this.jid.asStringWithBare();
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getSubscription() {
		return this.subscription;
	}

	public String getRole() {
		return ItemRole.VISITOR.toString();
	}

	public String getAffiliation() {
		return ItemAffiliation.MEMBER.toString();
	}

	public boolean in(String... subscriptions) {
		return false;
	}

	public boolean in(RosterSubscription... subscriptions) {
		return false;
	}

	public boolean isActivate() {
		return this.activate;
	}

	@Override
	public boolean isAsk() {
		return false;
	}

	@Override
	public Map<String, Object> plus() {
		return fieldPlus;
	}

	@Override
	public String[] asGroups() {
		return null;
	}

	@Override
	public <T extends Relation> T cast(Class<T> clazz) {
		return clazz.cast(this);
	}
}