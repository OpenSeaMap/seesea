/**
Copyright (c) 2016, Jens Kübler
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
package net.sf.seesea.data.sync;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="track")
public class Track {
	
	public long id;

	public String fileName;

	public int upload_state;

	public String fileType;

	public String compression;

	public String delete;
	
	public long vesselconfigid;

	public long containertrack;
	
	public long license;
	
//	@XmlElement(name = "uploaddate", required = true)
//	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public long uploadDate;

	public Track() {
		
	}
	
	public Track(long trackId, String name, int upload_state, String filetype, String compression, long containertrack) {
		super();
		this.id = trackId;
		this.fileName = name;
		this.upload_state = upload_state;
		this.fileType = filetype;
		this.compression = compression;
		this.containertrack = containertrack;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getUpload_state() {
		return upload_state;
	}

	public void setUpload_state(int upload_state) {
		this.upload_state = upload_state;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getCompression() {
		return compression;
	}

	public void setCompression(String compression) {
		this.compression = compression;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public long getVesselconfigid() {
		return vesselconfigid;
	}

	public void setVesselconfigid(long vesselconfigid) {
		this.vesselconfigid = vesselconfigid;
	}

	public long getContainertrack() {
		return containertrack;
	}

	public void setContainertrack(long containertrack) {
		this.containertrack = containertrack;
	}

	public long getLicense() {
		return license;
	}

	public void setLicense(long license) {
		this.license = license;
	}

	public long getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(long uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	

}
