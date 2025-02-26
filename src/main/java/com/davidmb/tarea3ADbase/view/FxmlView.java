package com.davidmb.tarea3ADbase.view;

import java.util.ResourceBundle;

public enum FxmlView {
	ADMIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("admin.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Admin.fxml";
		}
	},
	PILGRIM {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("pilgrim.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Pilgrim.fxml";
		}
	},
	STOP {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("stop.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Stop.fxml";
		}
	},
	FORGOT_PASSWORD {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("forgotpassword.title");
		}
		
		@Override
		public String getFxmlFile() {
			return "/fxml/ForgotPassword.fxml";
		}
	},
	LOGIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("login.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Login.fxml";
		}
	},
	SERVICES {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("services.title");
		}

		@Override
		public String getFxmlFile() {
			return"/fxml/Services.fxml";
		}
	},
	STAMPCARD {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("stampCard.title");
		}

		@Override
		public String getFxmlFile() {
			return"/fxml/StampCard.fxml";
		}
	},
	SENDHOME {
		@Override
        public String getTitle() {
            return getStringFromResourceBundle("sendhome.title");
        }

        @Override
        public String getFxmlFile() {
            return"/fxml/SendHome.fxml";
        }
	},
	VIEWSENDHOMES {
		@Override
        public String getTitle() {
            return getStringFromResourceBundle("sendhome.title");
        }

        @Override
        public String getFxmlFile() {
            return"/fxml/SendHomeTable.fxml";
        }
	},
	REGISTER_PILGRIM {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("registerpilgrim.title");
		}

		@Override
		public String getFxmlFile() {
			return"/fxml/RegisterPilgrim.fxml";
		}
	};

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}
