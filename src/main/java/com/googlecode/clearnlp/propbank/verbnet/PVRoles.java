/**
* Copyright (c) 2009-2012, Regents of the University of Colorado
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package com.googlecode.clearnlp.propbank.verbnet;

import java.util.ArrayList;
import java.util.Collections;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.googlecode.clearnlp.propbank.PBArg;
import com.googlecode.clearnlp.propbank.PBInstance;
import com.googlecode.clearnlp.propbank.PBLib;
import com.googlecode.clearnlp.util.UTXml;


@SuppressWarnings("serial")
public class PVRoles extends ArrayList<PVRole>
{
	static public final String ATTR_VNCLS = "vncls";
	static public final String DELIM      = ";";
	String s_vncls;
	
	/** From a PB2VN mapping file. */
	public PVRoles(Element eRoles, String vncls)
	{
		NodeList list = eRoles.getElementsByTagName(PVMap.E_ROLE);
		int i, size = list.getLength();
		
		s_vncls = vncls;

		for (i=0; i<size; i++)
			add(new PVRole((Element)list.item(i)));
	}
	
	/** From a PropBank frameset file. */
	public PVRoles(NodeList nRoles, String vncls)
	{
		int i, size = nRoles.getLength();
		String n, f, vntheta;
		Element eRole;

		s_vncls = vncls;

		for (i=0; i<size; i++)
		{
			eRole   = (Element)nRoles.item(i);
			vntheta = getVntheta(eRole.getElementsByTagName(PVMap.E_VNROLE), vncls);
			
			if (!vntheta.isEmpty())
			{
				n = UTXml.getTrimmedAttribute(eRole, PVRole.ATTR_N);
				f = UTXml.getTrimmedAttribute(eRole, PVRole.ATTR_F);
				
				add(new PVRole(n, f, vntheta));
			}
		}
	}
	
	/** Called by {@link PVRoles#PVRoles(NodeList, String)}. */
	private String getVntheta(NodeList nVnroles, String vncls)
	{
		int i, size = nVnroles.getLength();
		Element eVnrole;
		String cls;
		
		for (i=0; i<size; i++)
		{
			eVnrole = (Element)nVnroles.item(i);
			cls = UTXml.getTrimmedAttribute(eVnrole, PVRoles.ATTR_VNCLS);
			
			if (cls.equals(vncls))
				return UTXml.getTrimmedAttribute(eVnrole, PVRole.ATTR_VNTHETA);
		}
		
		return "";
	}
	
	public void addVBRoles(PBInstance instance)
	{
		instance.roleset += DELIM + s_vncls;
		PVRole pvRole;
		
		for (PBArg pbArg : instance.getArgs())
		{	
			pvRole = getRole(pbArg);
			
			if (pvRole != null)
				pbArg.label += DELIM + pvRole.vntheta;
		}
	}
	
	public PVRole getRole(PBArg pbArg)
	{
		if (PBLib.isNumberedArgument(pbArg))
		{
			for (PVRole pvRole : this)
			{
				if (pvRole.isArgN(pbArg.label))
					return pvRole;
			}
		}
		
		return null;
	}
	
	public PVRole getRole(String n)
	{
		for (PVRole role : this)
		{
			if (role.n.equals(n))
				return role;
		}
		
		return null;
	}
	
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		Collections.sort(this);
		
		for (PVRole pvRole : this)
		{
			build.append("\n");
			build.append("      ");
			build.append(pvRole);
		}
		
		return UTXml.getTemplate(PVMap.E_ROLES, build.substring(1), "    ", ATTR_VNCLS, s_vncls);
	}
}
