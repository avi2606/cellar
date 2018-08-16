// The root URL for the RESTful services
var rootURL = "http://localhost:8080/cellar/rest/employee";

var currentEmployee;

// Retrieve emp list when application starts 
findAll();

// Nothing to delete in initial application state
$('#btnDelete').hide();

// Register listeners
$('#btnSearch').click(function() {
	search($('#searchKey').val());
	return false;
});

// Trigger search when pressing 'Return' on search key input field
$('#searchKey').keypress(function(e){
	if(e.which == 13) {
		search($('#searchKey').val());
		e.preventDefault();
		return false;
    }
});

$('#btnAdd').click(function() {
	newEmployee();
	return false;
});

$('#btnSave').click(function() {
	if ($('#empID').val() == '')
		addEmployee();
	else
		updateEmployee();
	return false;
});

$('#btnDelete').click(function() {
	deleteEmployee();
	return false;
});

$('#empList a').live('click', function() {
	findById($(this).data('identity'));
});

// Replace broken images with generic emp bottle
$("img").error(function(){
  $(this).attr("src", "pics/generic.jpg");

});

function search(searchKey) {
	if (searchKey == '') 
		findAll();
	else
		findByName(searchKey);
}

function newEmployee() {
	$('#btnDelete').hide();
	currentEmployee = {};
	renderDetails(currentEmployee); // Display empty form
}

function findAll() {
	console.log('findAll');
	$.ajax({
		type: 'GET',
		url: rootURL,
		dataType: "json", // data type of response
		success: renderList
	});
}

function findByName(searchKey) {
	console.log('findByName: ' + searchKey);
	$.ajax({
		type: 'GET',
		url: rootURL + '/search/' + searchKey,
		dataType: "json",
		success: renderList 
	});
}

function findById(id) {
	console.log('findById: ' + id);
	$.ajax({
		type: 'GET',
		url: rootURL + '/' + id,
		dataType: "json",
		success: function(data){
			$('#btnDelete').show();
			console.log('findById success: ' + data.name);
			currentEmployee = data;
			renderDetails(currentEmployee);
		}
	});
}

function addEmployee() {
	console.log('addEmployee');
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL,
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			alert('Employee created successfully');
			$('#btnDelete').show();
			$('#empID').val(data.id);
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('addEmployee error: ' + textStatus);
		}
	});
}

function updateEmployee() {
	console.log('updateEmployee');
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
		url: rootURL + '/' + $('#empID').val(),
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			alert('Employee updated successfully');
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('updateEmployee error: ' + textStatus);
		}
	});
}

function deleteEmployee() {
	console.log('deleteEmployee');
	$.ajax({
		type: 'DELETE',
		url: rootURL + '/' + $('#empID').val(),
		success: function(data, textStatus, jqXHR){
			alert('Employee deleted successfully');
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('deleteEmployee error');
		}
	});
}

function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [data]);

	$('#empList li').remove();
	$.each(list, function(index, emp) {
		$('#empList').append('<li><a href="#" data-identity="' + emp.id + '">'+emp.name+'</a></li>');
	});
}

function renderDetails(emp) {
	$('#empID').val(emp.id);
	$('#empName').val(emp.name);
	$('#empDept').val(emp.dept);
	$('#empSalary').val(emp.salary);
	
}

// Helper function to serialize all the form fields into a JSON string
function formToJSON() {
	var empID = $('#empID').val();
	return JSON.stringify({
		"id": empID == "" ? null : empID, 
		"name": $('#name').val(), 
		"dept": $('#empDept').val(),
		"salary": $('#empSalary').val(),
		
		});
}
