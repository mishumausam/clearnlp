/**
* Copyright 2012 University of Massachusetts Amherst
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
*   
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.googlecode.clearnlp.dependency;


/**
 * @since 1.3.0
 * @author Jinho D. Choi ({@code jdchoi77@gmail.com})
 */
public class DEPLabel
{
	static final String DELIM   = "_";
	
	public String arc;
	public String list;
	public String deprel;

	public DEPLabel(String label)
	{
		int idx = label.indexOf(DELIM);
		
		arc    = label.substring(0, idx);
		list   = label.substring(idx+1, idx = label.lastIndexOf(DELIM));
		deprel = label.substring(idx+1);
	}
	
	public DEPLabel(String arc, String deprel)
	{
		this.arc    = arc;
		this.list   = "";
		this.deprel = deprel;
	}
	
	public boolean isArc(String label)
	{
		return arc.equals(label);
	}
	
	public boolean isList(String label)
	{
		return list.equals(label);
	}
	
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		
		build.append(arc);		build.append(DELIM);
		build.append(list);		build.append(DELIM);
		build.append(deprel);
		
		return build.toString();
	}
}
