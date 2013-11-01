package com.sisi.addressing.hash;

import org.apache.commons.collections.map.LRUMap;

import com.sisi.addressing.Addressing;
import com.sisi.context.Context;
import com.sisi.context.JID;

/**
 * @author kim 2013-11-1
 */
public class SimpleHashAddressing implements Addressing {

	private LRUMap addressings;

	public SimpleHashAddressing(Integer poolSize) {
		this.addressings = new LRUMap(poolSize);
	}

	public Context join(Context context) {
		this.addressings.put(context.jid().asStringWithLoop(), context);
		return context;
	}

	@Override
	public Context find(JID jid) {
		return (Context) this.addressings.get(jid.asStringWithLoop());
	}

	public Boolean isLogin(JID jid) {
		return this.find(jid) != null;
	}
}
