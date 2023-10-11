# Beim BUILD und RUN beachten
Folgender Eintrag

>  BACKEND_TOASTMASTER_URI=http://localhost:8080/api/toast-master-manager

gehört in die Environment Variable beim Frontend

* Löschen geht nur mit Admin Rechten
  * hierzu in der Tabelle users die Role auf 'ADMIN' setzen
* Beim User update können folgende Felder geändert werden
  * Firstname
  * Lastname
  * email
  * Role
* Password kann aktuell noch nicht geändert werden
