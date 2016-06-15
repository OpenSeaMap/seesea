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

package net.sf.seesea.data.postprocessing.filter;

import java.util.Comparator;

import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.track.api.data.ITrackFile;

public class TrackFileComparator implements Comparator<ITrackFile> {

	@Override
	public int compare(ITrackFile fileA, ITrackFile fileB) {
		MeasuredPosition3D endB = fileB.getEnd();
		MeasuredPosition3D endA = fileA.getEnd();
		MeasuredPosition3D startA = fileA.getStart();
		MeasuredPosition3D startB = fileB.getStart();
		if(endB.getTime() != null && endA.getTime() != null && startB.getTime() != null && startA.getTime() != null &&
				endA.getTime().getTime() - startA.getTime().getTime() > 0 && (endB.getTime().getTime() - startB.getTime().getTime()) > 0) {
			if(endA.getTime().getTime() == endB.getTime().getTime() && startA.getTime().getTime() == startB.getTime().getTime()) {
				return compareNameId(fileA, fileB);
			}
			// fail safe handling for overlapping times
			if(startA.getTime().getTime() < startB.getTime().getTime() && endA.getTime().getTime() > startB.getTime().getTime()) {
				return compareNameId(fileA, fileB);
			}
			if(startB.getTime().getTime() < startA.getTime().getTime() && endB.getTime().getTime() > startA.getTime().getTime()) {
				return compareNameId(fileA, fileB);
			}
			
			
			long a = (endA.getTime().getTime() - startB.getTime().getTime());
			if(a < 0 ) {
				return -1;
			} else if(a > 0) {
				return 1;
			}
		}
		return compareNameId(fileA, fileB);
	}

	private int compareNameId(ITrackFile fileA, ITrackFile fileB) {
		int compareTo = fileA.getTrackQualifier().compareTo(fileB.getTrackQualifier());
		if(compareTo == 0) {
			return (int) (fileA.getTrackId() - fileB.getTrackId());
		} else {
			return compareTo;
		}
	}

}
