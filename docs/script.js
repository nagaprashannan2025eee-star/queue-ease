const API_BASE_URL = "http://localhost:8080/api";


// =====================================================
// GLOBAL VARIABLES
// =====================================================

let selectedDoctorId = 1;

let currentPatient = null;

let currentToken = null;

let loginType = "doctor";


// =====================================================
// LOGIN TYPE
// =====================================================

function selectLoginType(type) {

    loginType = type;

    const doctorTab =
        document.getElementById("doctorLoginTab");

    const patientTab =
        document.getElementById("patientLoginTab");

    const doctorForm =
        document.getElementById("doctorLoginForm");

    const patientForm =
        document.getElementById("patientLoginForm");


    if (type === "doctor") {

        doctorTab.classList.add("active");
        patientTab.classList.remove("active");

        doctorForm.classList.remove("hidden");
        patientForm.classList.add("hidden");

    } else {

        patientTab.classList.add("active");
        doctorTab.classList.remove("active");

        patientForm.classList.remove("hidden");
        doctorForm.classList.add("hidden");
    }


    document.getElementById("loginMessage").textContent = "";
}


// =====================================================
// DOCTOR LOGIN
// =====================================================

document
    .getElementById("doctorLoginForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();


        const username =
            document
                .getElementById("doctorUsername")
                .value
                .trim();

        const password =
            document
                .getElementById("doctorPassword")
                .value;


        const message =
            document.getElementById("loginMessage");


        /*
         * Temporary demo login
         *
         * Username : doctor
         * Password : doctor123
         */

        if (
            username === "doctor" &&
            password === "doctor123"
        ) {

            loginType = "doctor";


            document
                .getElementById("loginPage")
                .classList.add("hidden");


            document
                .getElementById("doctorApp")
                .classList.remove("hidden");


            document
                .getElementById("doctorWelcome")
                .textContent = "👨‍⚕️ Dr. Doctor";


            await loadDoctors();

            await loadDoctorDashboard();

            await loadDoctorQueue();

            return;
        }


        message.textContent =
            "Invalid doctor username or password.";

        message.style.color = "red";

    });


// =====================================================
// PATIENT LOGIN
// =====================================================

document
    .getElementById("patientLoginForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();


        const phone =
            document
                .getElementById("patientLoginPhone")
                .value
                .trim();

        const password =
            document
                .getElementById("patientLoginPassword")
                .value;


        const message =
            document.getElementById("loginMessage");


        /*
         * Temporary patient password:
         *
         * patient123
         */

        if (password !== "patient123") {

            message.textContent =
                "Invalid patient password.";

            message.style.color = "red";

            return;
        }


        try {

            const response =
                await fetch(
                    `${API_BASE_URL}/patients`
                );


            if (!response.ok) {
                throw new Error("Unable to load patients");
            }


            const patients =
                await response.json();


            const patient =
                patients.find(
                    p => p.phone === phone
                );


            if (!patient) {

                message.textContent =
                    "Patient with this phone number was not found.";

                message.style.color = "red";

                return;
            }


            currentPatient = patient;


            document
                .getElementById("loginPage")
                .classList.add("hidden");


            document
                .getElementById("patientApp")
                .classList.remove("hidden");


            document
                .getElementById("patientWelcome")
                .textContent =
                `🧑 ${patient.name}`;


            await loadDoctors();

            await loadPatientToken();

        } catch (error) {

            console.error(error);

            message.textContent =
                "Unable to connect to the server.";

            message.style.color = "red";
        }

    });


// =====================================================
// LOGOUT
// =====================================================

function logout() {

    currentPatient = null;

    currentToken = null;


    document
        .getElementById("doctorApp")
        .classList.add("hidden");


    document
        .getElementById("patientApp")
        .classList.add("hidden");


    document
        .getElementById("loginPage")
        .classList.remove("hidden");


    document
        .getElementById("doctorLoginForm")
        .reset();


    document
        .getElementById("patientLoginForm")
        .reset();


    document
        .getElementById("loginMessage")
        .textContent = "";


    selectLoginType("doctor");
}


// =====================================================
// LOAD DOCTORS
// =====================================================

async function loadDoctors() {

    try {

        const response =
            await fetch(
                `${API_BASE_URL}/doctors`
            );


        if (!response.ok) {
            throw new Error("Doctor loading failed");
        }


        const doctors =
            await response.json();


        /*
         * Select first active doctor
         */

        const activeDoctor =
            doctors.find(
                doctor => doctor.active === true
            );


        if (activeDoctor) {

            selectedDoctorId =
                activeDoctor.id;
        }


        populateDoctorSelect(doctors);


    } catch (error) {

        console.error(
            "Doctor loading error:",
            error
        );
    }
}


// =====================================================
// POPULATE DOCTOR SELECT
// =====================================================

function populateDoctorSelect(doctors) {

    const select =
        document.getElementById("tokenDoctor");


    if (!select) {
        return;
    }


    select.innerHTML = `
        <option value="">
            Select Doctor
        </option>
    `;


    doctors
        .filter(doctor => doctor.active === true)
        .forEach(doctor => {

            const option =
                document.createElement("option");


            option.value =
                doctor.id;


            option.textContent =
                `${doctor.name} - ${doctor.specialization}`;


            select.appendChild(option);

        });
}


// =====================================================
// DOCTOR SECTION NAVIGATION
// =====================================================

function showDoctorSection(sectionId) {

    document
        .querySelectorAll("#doctorApp .section")
        .forEach(section => {

            section.classList.remove("active");

        });


    const section =
        document.getElementById(sectionId);


    if (section) {

        section.classList.add("active");

    }


    if (sectionId === "doctorDashboard") {

        loadDoctorDashboard();

    }


    if (sectionId === "doctorQueue") {

        loadDoctorQueue();

    }


    if (sectionId === "doctorPatients") {

        loadDoctorPatients();

    }
}


// =====================================================
// PATIENT SECTION NAVIGATION
// =====================================================

function showPatientSection(sectionId) {

    document
        .querySelectorAll("#patientApp .section")
        .forEach(section => {

            section.classList.remove("active");

        });


    const section =
        document.getElementById(sectionId);


    if (section) {

        section.classList.add("active");

    }


    if (sectionId === "patientToken") {

        loadDoctors();

    }


    if (sectionId === "myToken") {

        loadPatientToken();

    }
}


// =====================================================
// DOCTOR DASHBOARD
// =====================================================

async function loadDoctorDashboard() {

    try {

        const response =
            await fetch(
                `${API_BASE_URL}/tokens/doctor/${selectedDoctorId}`
            );


        if (!response.ok) {
            throw new Error("Queue loading failed");
        }


        const queue =
            await response.json();


        const waiting =
            queue.filter(
                token =>
                    token.status === "WAITING"
            );


        const completed =
            queue.filter(
                token =>
                    token.status === "COMPLETED"
            );


        const serving =
            queue.find(
                token =>
                    token.status === "SERVING"
            );


        document
            .getElementById("doctorWaitingCount")
            .textContent =
            waiting.length;


        document
            .getElementById("doctorCompletedCount")
            .textContent =
            completed.length;


        document
            .getElementById("doctorTotalCount")
            .textContent =
            queue.length;


        document
            .getElementById("doctorCurrentToken")
            .textContent =
            serving
                ? `#${serving.tokenNumber}`
                : "-";


        const currentPatientInfo =
            document.getElementById(
                "currentPatientInfo"
            );


        if (serving) {

            currentPatientInfo.innerHTML = `

                <strong>
                    Token #${serving.tokenNumber}
                </strong>

                <br><br>

                Patient:
                ${serving.patientName}

                <br>

                Priority:
                ${serving.priority}

            `;

        } else {

            currentPatientInfo.textContent =
                "No patient is currently being served.";

        }


    } catch (error) {

        console.error(
            "Doctor dashboard error:",
            error
        );

    }
}


// =====================================================
// DOCTOR QUEUE
// =====================================================

async function loadDoctorQueue() {

    const queueList =
        document.getElementById(
            "doctorQueueList"
        );


    if (!queueList) {
        return;
    }


    try {

        const response =
            await fetch(
                `${API_BASE_URL}/tokens/doctor/${selectedDoctorId}`
            );


        if (!response.ok) {
            throw new Error("Queue loading failed");
        }


        const queue =
            await response.json();


        renderDoctorQueue(queue);


        const serving =
            queue.find(
                token =>
                    token.status === "SERVING"
            );


        document
            .getElementById(
                "doctorQueueCurrentToken"
            )
            .textContent =
            serving
                ? `#${serving.tokenNumber}`
                : "-";


    } catch (error) {

        console.error(error);


        queueList.innerHTML = `

            <p class="empty">
                Unable to load queue.
            </p>

        `;

    }
}


// =====================================================
// RENDER DOCTOR QUEUE
// =====================================================

function renderDoctorQueue(queue) {

    const queueList =
        document.getElementById(
            "doctorQueueList"
        );


    if (
        !Array.isArray(queue) ||
        queue.length === 0
    ) {

        queueList.innerHTML = `

            <p class="empty">
                No patients in today's queue.
            </p>

        `;

        return;
    }


    queueList.innerHTML = "";


    queue.forEach(token => {

        const item =
            document.createElement("div");


        item.className =
            "queue-item";


        item.innerHTML = `

            <div class="token-number">

                #${token.tokenNumber}

            </div>


            <div class="token-info">

                <h4>
                    ${token.patientName || "Patient"}
                </h4>

                <p>
                    Priority:
                    ${token.priority}
                </p>

                <p>
                    Wait:
                    ${token.estimatedWaitMinutes || 0}
                    minutes
                </p>

            </div>


            <div class="status ${token.status}">

                ${token.status}

            </div>

        `;


        queueList.appendChild(item);

    });
}


// =====================================================
// CALL NEXT PATIENT
// =====================================================

async function callNextPatient() {

    try {

        const response =
            await fetch(
                `${API_BASE_URL}/tokens/doctor/${selectedDoctorId}/next`,
                {
                    method: "PUT"
                }
            );


        if (response.status === 204) {

            alert(
                "No waiting patients."
            );

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Unable to call next patient"
            );

        }


        const token =
            await response.json();


        alert(
            `Token #${token.tokenNumber} - ${token.patientName} is now being served.`
        );


        await loadDoctorDashboard();

        await loadDoctorQueue();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to call next patient."
        );

    }
}


// =====================================================
// LOAD DOCTOR PATIENTS
// =====================================================

async function loadDoctorPatients() {

    const patientList =
        document.getElementById(
            "doctorPatientList"
        );


    if (!patientList) {
        return;
    }


    try {

        const response =
            await fetch(
                `${API_BASE_URL}/patients`
            );


        if (!response.ok) {

            throw new Error(
                "Patient loading failed"
            );

        }


        const patients =
            await response.json();


        if (
            !Array.isArray(patients) ||
            patients.length === 0
        ) {

            patientList.innerHTML = `

                <p class="empty">
                    No registered patients.
                </p>

            `;

            return;
        }


        patientList.innerHTML = "";


        patients.forEach(patient => {

            const item =
                document.createElement("div");


            item.className =
                "patient-list-item";


            item.innerHTML = `

                <div>

                    <div class="patient-name">

                        ${patient.name}

                    </div>

                    <div class="patient-details">

                        Age: ${patient.age}
                        &nbsp; | &nbsp;
                        Gender: ${patient.gender}

                    </div>

                </div>


                <div class="patient-details">

                    📞 ${patient.phone}

                </div>

            `;


            patientList.appendChild(item);

        });


    } catch (error) {

        console.error(error);


        patientList.innerHTML = `

            <p class="empty">
                Unable to load patients.
            </p>

        `;

    }
}


// =====================================================
// PATIENT TOKEN GENERATION
// =====================================================

document
    .getElementById("tokenForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();


        if (!currentPatient) {

            alert(
                "Patient login required."
            );

            return;
        }


        const doctorId =
            Number(
                document
                    .getElementById("tokenDoctor")
                    .value
            );


        const priority =
            document
                .getElementById("tokenPriority")
                .value;


        const message =
            document.getElementById(
                "tokenMessage"
            );


        if (!doctorId) {

            message.innerHTML = `

                <div class="token-success">

                    Please select a doctor.

                </div>

            `;

            return;
        }


        try {

            const response =
                await fetch(
                    `${API_BASE_URL}/tokens`,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({

                            doctorId:
                                doctorId,

                            patientId:
                                currentPatient.id,

                            priority:
                                priority

                        })
                    }
                );


            const data =
                await response.json();


            if (!response.ok) {

                message.innerHTML = `

                    <div
                        class="token-success"
                        style="
                            background:#fef2f2;
                            border-color:#fecaca;
                        ">

                        <h3
                            style="color:#b91c1c">

                            Token Generation Failed

                        </h3>

                        <p>
                            ${data.message ||
                            "Unable to generate token."}
                        </p>

                    </div>

                `;

                return;
            }


            currentToken = data;


            message.innerHTML = `

                <div class="token-success">

                    <h3>
                        🎉 Token Generated Successfully
                    </h3>


                    <div class="big-token">

                        #${data.tokenNumber}

                    </div>


                    <p>
                        Doctor:
                        ${data.doctorName}
                    </p>


                    <p>
                        Priority:
                        ${data.priority}
                    </p>


                    <p>
                        Estimated Wait:
                        ${data.estimatedWaitMinutes || 0}
                        minutes
                    </p>

                </div>

            `;


            await loadPatientToken();


        } catch (error) {

            console.error(error);


            message.innerHTML = `

                <div class="token-success">

                    <h3
                        style="color:#b91c1c">

                        Server Error

                    </h3>

                    <p>
                        Unable to connect to server.
                    </p>

                </div>

            `;

        }

    });


// =====================================================
// LOAD PATIENT TOKEN
// =====================================================

async function loadPatientToken() {

    const details =
        document.getElementById(
            "myTokenDetails"
        );


    if (!details || !currentPatient) {
        return;
    }


    try {

        /*
         * Get all doctors
         */

        const doctorsResponse =
            await fetch(
                `${API_BASE_URL}/doctors`
            );


        const doctors =
            await doctorsResponse.json();


        /*
         * Find patient's token
         * by checking each doctor's queue.
         */

        let foundToken = null;


        for (const doctor of doctors) {

            const response =
                await fetch(
                    `${API_BASE_URL}/tokens/doctor/${doctor.id}`
                );


            if (!response.ok) {
                continue;
            }


            const queue =
                await response.json();


            const token =
                queue.find(
                    t =>
                        t.patientId === currentPatient.id &&
                        (
                            t.status === "WAITING" ||
                            t.status === "SERVING"
                        )
                );


            if (token) {

                foundToken = token;

                break;

            }
        }


        if (!foundToken) {

            details.innerHTML = `

                <p>
                    You currently have no active token.
                </p>

            `;

            return;
        }


        currentToken = foundToken;


        details.innerHTML = `

            <div>

                <p>
                    Your Token
                </p>


                <div class="my-token-number">

                    #${foundToken.tokenNumber}

                </div>


                <div class="my-token-info">

                    <strong>
                        Doctor:
                    </strong>

                    ${foundToken.doctorName}

                    <br>


                    <strong>
                        Status:
                    </strong>

                    ${foundToken.status}

                    <br>


                    <strong>
                        Priority:
                    </strong>

                    ${foundToken.priority}

                    <br>


                    <strong>
                        Estimated Wait:
                    </strong>

                    ${foundToken.estimatedWaitMinutes || 0}
                    minutes

                </div>

            </div>

        `;


    } catch (error) {

        console.error(
            "Patient token error:",
            error
        );


        details.innerHTML = `

            <p class="empty">

                Unable to load your token.

            </p>

        `;

    }
}


// =====================================================
// INITIAL STATE
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        /*
         * Make sure only login page
         * is visible initially.
         */

        document
            .getElementById("loginPage")
            .classList.remove("hidden");


        document
            .getElementById("doctorApp")
            .classList.add("hidden");


        document
            .getElementById("patientApp")
            .classList.add("hidden");


        selectLoginType("doctor");

    }
);
               