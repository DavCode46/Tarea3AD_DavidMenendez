package com.davidmb.tarea3ADbase.models;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "backupCarnets")
public class BackupCarnets {

	@Id
	private String id;
	private String fileName;
	private List<CarnetBackupJson> carnets;

	public BackupCarnets() {
		this.fileName = "backupscarnet";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<CarnetBackupJson> getCarnets() {
		return carnets;
	}

	public void setCarnets(List<CarnetBackupJson> carnets) {
		this.carnets = carnets;
	}

	public static class CarnetBackupJson {

		private String id;
		private Long pilgrimId;
		private String pilgrimName;
		private String pilgrimNationality;
		private LocalDate dOfIssue;
		private String issuedIn;
		private LocalDate today;
		private double distance;
		private List<StopJson> stops;
		private List<StayJson> stays;
		private LocalDate backupDate;

		public CarnetBackupJson() {
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Long getPilgrimId() {
			return pilgrimId;
		}

		public void setPilgrimId(Long pilgrimId) {
			this.pilgrimId = pilgrimId;
		}

		public String getPilgrimName() {
			return pilgrimName;
		}

		public void setPilgrimName(String pilgrimName) {
			this.pilgrimName = pilgrimName;
		}
		
		public String getPilgrimNationality() {
			return pilgrimNationality;
		}
		
		public void setPilgrimNationality(String pilgrimNationality) {
            this.pilgrimNationality = pilgrimNationality;
		}

		public LocalDate getdOfIssue() {
			return dOfIssue;
		}

		public void setdOfIssue(LocalDate dOfIssue) {
			this.dOfIssue = dOfIssue;
		}

		public String getIssuedIn() {
			return issuedIn;
		}

		public void setIssuedIn(String issuedIn) {
			this.issuedIn = issuedIn;
		}

		public LocalDate getToday() {
			return today;
		}

		public void setToday(LocalDate today) {
			this.today = today;
		}

		public double getDistance() {
			return distance;
		}

		public void setDistance(double distance) {
			this.distance = distance;
		}

		public List<StopJson> getStops() {
			return stops;
		}

		public void setStops(List<StopJson> stops) {
			this.stops = stops;
		}

		public List<StayJson> getStays() {
			return stays;
		}

		public void setStays(List<StayJson> stays) {
			this.stays = stays;
		}

		public LocalDate getBackupDate() {
			return backupDate;
		}

		public void setBackupDate(LocalDate backupDate) {
			this.backupDate = backupDate;
		}
		
		public static class StopJson {
			private int order;
			private String name;
			private String region;

			public StopJson() {
			}

			public int getOrder() {
				return order;
			}

			public void setOrder(int order) {
				this.order = order;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getRegion() {
				return region;
			}

			public void setRegion(String region) {
				this.region = region;
			}
		}

		public static class StayJson {
			private Long id;
			private LocalDate stayDate;
			private String stop;
			private boolean vip;

			public StayJson() {
			}

			public Long getId() {
				return id;
			}

			public void setId(Long id) {
				this.id = id;
			}

			public LocalDate getStayDate() {
				return stayDate;
			}

			public void setStayDate(LocalDate stayDate) {
				this.stayDate = stayDate;
			}

			public String getStop() {
				return stop;
			}

			public void setStop(String stop) {
				this.stop = stop;
			}

			public boolean isVip() {
				return vip;
			}

			public void setVip(boolean vip) {
				this.vip = vip;
			}
		}
	}	
}
