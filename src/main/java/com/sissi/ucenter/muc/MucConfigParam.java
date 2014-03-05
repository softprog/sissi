package com.sissi.ucenter.muc;

import java.util.Map;

import com.sissi.context.JID;

/**
 * @author kim 2014年3月5日
 */
public interface MucConfigParam {

	public JID user();

	public boolean level();

	public boolean creator();
	
	public boolean hidden(boolean compute);

	public RelationMuc relation();

	public Map<String, Object> configs();
}
