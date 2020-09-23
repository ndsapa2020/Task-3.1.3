window.onload = fillTable();

function fillTable() {
    cleanTable();
    fetch("/api/users/", {method: 'GET'})
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            const insertDiv = $("#users_Table_Content");
            data.forEach(function (el) {
                const v = $("<tr id='rrr'></tr>").appendTo(insertDiv);
                v.attr('id', 'user-row-' + el.id);

                $("<td></td>").text(el.id).appendTo(v);
                let loginTd = $("<td id='rrr'></td>").text(el.login).appendTo(v);
                loginTd.attr('id', 'user-login-' + el.id);
                let lastNameTd = $("<td id='rrr' ></td>").text(el.lastName).appendTo(v);
                lastNameTd.attr('id', 'user-lastname-' + el.id);
                let ageTd = $("<td id='rrr'></td>").text(el.age).appendTo(v);
                ageTd.attr('id', 'user-age-' + el.id);
                let emailTd = $("<td id='rrr'></td>").text(el.email).appendTo(v);
                emailTd.attr('id', 'user-email-' + el.id);

                const r_div = $("<td></td>").appendTo(v);
                let rolesList = el.roles;

                const rr = $("<div></div>").appendTo(r_div);

                rolesList.forEach(function (aRole) {
                    if (aRole.role === 'ROLE_ADMIN') {
                        $("<div></div>").text('Admin').appendTo(rr);
                    }
                    if (aRole.role === 'ROLE_USER') {
                        $("<div></div>").text('User').appendTo(rr);
                    }
                })
                const editTd = $("<td></td>").appendTo(v);

                const editButtonVsId = $("<button type='button' class='btn btn-primary' data-toggle='modal' data-target='#editModal' " +
                    "id='editBut' name='id' value='1'></button>").text('Edit').appendTo(editTd);
                editButtonVsId.attr('value', el.id);

                const deleteTd = $("<td></td>").appendTo(v);
                const deleteButtonVsId = $("<button type='button' class='btn btn-primary' data-toggle='modal' data-target='#deleteModal' " +
                    "id='deleteBut' name='id' value='1'></button>").text('Delete').appendTo(deleteTd);
                deleteButtonVsId.attr('value', el.id);
            })
            console.log("api_button ok");
        })
}

// ==================== Edit =================

const editM = $("#editModal");
editM.on('show.bs.modal', function (e) {
    let userId = $(e.relatedTarget).attr('value');
    console.log(userId);

    const requestURL = '/api/users/';
    let url = requestURL + userId;

    fetch(url, {method: 'GET'})
        .then((response) => {
            return response.json();
        })
        .then((response_data) => {
            let editId = response_data.id;
            let editLogin = response_data.login;
            let editLastName = response_data.lastName;
            let editAge = response_data.age;
            let editEmail = response_data.email;

            console.log('Info about editing user DATA:')
            console.log(editId);
            console.log(editLogin);
            console.log(editLastName);
            console.log(editAge);
            console.log(editEmail);

            $('#editId').val(editId);
            $('#editLogin').val(editLogin);
            $('#editLastName').val(editLastName);
            $('#editAge').val(editAge);
            $('#editEmail').val(editEmail);
        });
});

// ============== Edit submit ========

function myEditSubmitFunction() {
    const requestURL = '/api/users/';
    let formElements = new FormData(editForm);

    let toEditData = {
        id: formElements.get("editId"),
        login: formElements.get("editLogin"),
        lastName: formElements.get("editLastName"),
        age: formElements.get("editAge"),
        email: formElements.get('editEmail'),
        password: formElements.get("editPassword"),
        isAdmin: formElements.get("editIsAdmin"),
        isUser: formElements.get("editIsUser")
    }

    let json_formElements = JSON.stringify(formElements);
    console.log('newData = ' + toEditData);
    let json_newData = JSON.stringify(toEditData);
    console.log('json_newData = ' + json_newData);
    console.log('formElements = ' + formElements);
    console.log('json_formElements = ' + json_formElements);

    let userId = formElements.get("editId");
    let url = requestURL + userId;

    sendRequest('PUT', url, json_newData)
        .then( () => {
            console.log('Try to change edit data/ userId ' + userId + ' ');

            let editLoginTd = '#user-login-' + userId;
            $(editLoginTd).text(formElements.get("editLogin"));

            let editLastNameTd = '#user-lastname-' + userId;
            $(editLastNameTd).text(formElements.get("editLastName"));

            let editAgeTd = '#user-age-' + userId;
            $(editAgeTd).text(formElements.get("editAge"));

            let editEmailTd = '#user-email-' + userId;
            $(editEmailTd).text(formElements.get("editEmail"));
        })
        .catch(err => console.log(err))

    async function sendRequest(method, url, body) {
        await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        });
    }
}

editM.on('hidden.bs.modal', function () {
    let form = $(this).find('form');
    console.log("closed modal function");
    form[0].reset();
});

// ========= Add new User  =========

const formSubmit = newUserForm;
formSubmit.onsubmit = async (e) => {
    e.preventDefault();

    const requestURL = '/api/users';
    let formElems = new FormData(formSubmit);       //object FormData
    console.log('formElem = ' + formElems);

    let newData = {
        login: formElems.get("newName"),
        lastName: formElems.get("newLastName"),
        age: formElems.get("newAge"),
        email: formElems.get('newEmail'),
        password: formElems.get("newPassword"),
        isAdmin: formElems.get("isAdmin"),
        isUser: formElems.get("isUser")
    }

    console.log('newData = ' + newData);
    let json_newData = JSON.stringify(newData);
    console.log('json_newData = ' + json_newData);

    sendRequest('POST', requestURL, json_newData).then(() => {
        $('#saveSuccessModal').modal({
            show: true
        });
        formSubmit.reset();
        console.log("cleaned new User form");
        fillTable();
    }).catch(err => console.log(err))

    async function sendRequest(method, url, body) {
        await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        }).then(() => {
        })
    }
};
// =================================

const deleteM = $("#deleteModal");
deleteM.on('show.bs.modal', function (e) {
    let userId = $(e.relatedTarget).attr('value');
    console.log(userId);
    const requestURL = '/api/users/';
    let url = requestURL + userId;

    fetch(url, {method: 'GET'})
        .then((response) => {
            return response.json();
        })
        .then((response_data) => {
            console.log(response_data);

            let editId = response_data.id;
            let editLogin = response_data.login;
            let editLastName = response_data.lastName;
            let editAge = response_data.age;
            let editEmail = response_data.email;

            console.log('Info about deleting user DATA:')
            console.log(editId);
            console.log(editLogin);
            console.log(editLastName);
            console.log(editAge);
            console.log(editEmail);

            $('#deleteId').val(editId);
            $('#deleteLogin').val(editLogin);
            $('#deleteLastName').val(editLastName);
            $('#deleteAge').val(editAge);
            $('#deleteEmail').val(editEmail);
        });

});

// delete submit function ===========================

function myDeleteSubmitFunction() {
    let form = document.forms.deleteForm;
    let userId = form.elements.deleteId.value;
    const requestURL = '/api/users/';
    let url = requestURL + userId;

    sendRequest('DELETE', url)
        .then(() => {
                console.log('User ' + userId + ' is deleted')
                let deleteTR = "#user-row-" + userId;
                $(deleteTR).remove();
            }
        )
        .catch(err => console.log(err))

    async function sendRequest(method, url) {
        fetch(url, {
            method: method
        })
            .then((response) => {
                return response;
            })
    }
}

function cleanTable() {
    let elem = $("#users_Table_Content");
    let tb = $("<tbody id='users_Table_Content'> </tbody>");
    elem.replaceWith(tb);
}