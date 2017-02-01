package org.osm.depth.upload.messages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="tide")
public class RoleChange {

	public Long id;
	
	public String user_name;

	public String requestedRole;
}
