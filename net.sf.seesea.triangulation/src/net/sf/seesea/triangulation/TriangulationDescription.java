/**
Copyright (c) 2013-2015, Jens Kübler
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package net.sf.seesea.triangulation;

import java.util.List;

import net.sf.seesea.geometry.IPolygon;
import net.sf.seesea.geometry.ITriangle;

/**
 * 
 */
public class TriangulationDescription implements ITriangulationDescription {


	private IPolygon border;
	private List<IPolygon> holes;
	private List<ITriangle> triangulation;

	
	
	public TriangulationDescription(IPolygon border, List<IPolygon> holes, List<ITriangle> triangulation) {
		super();
		this.border = border;
		this.holes = holes;
		this.triangulation = triangulation;
	}

	/* (non-Javadoc)
	 * @see net.sf.seesea.data.postprocessing.triangulation.persistence.ITriangulationDescription#getBorder()
	 */
	@Override
	public IPolygon getBorder() {
		return border;
	}

	/* (non-Javadoc)
	 * @see net.sf.seesea.data.postprocessing.triangulation.persistence.ITriangulationDescription#getHoles()
	 */
	@Override
	public List<IPolygon> getHoles() {
		return holes;
	}

	/* (non-Javadoc)
	 * @see net.sf.seesea.data.postprocessing.triangulation.persistence.ITriangulationDescription#getTriangulation()
	 */
	@Override
	public List<ITriangle> getTriangulation() {
		return triangulation;
	}

}
