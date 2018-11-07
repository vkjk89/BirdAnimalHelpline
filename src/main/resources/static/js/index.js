function admin(){
	if (document.FirstForm.Username.value == "admin" && document.FirstForm.Password.value == "admin")
		{
			window.location="/html/Admin.html";
		}
	else window.location = "/html/Volunteer.html"
}