document.querySelector("form").addEventListener("submit", function(e) {
    e.preventDefault();

    console.log("Submit intercepté !");
});

async function login() {
    let email = document.getElementById('email').value
    let password = await sha256(document.getElementById('password').value).then(hash => {return hash})
    const xhr = new XMLHttpRequest()
    xhr.open('GET', `Connection?action=connect&email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`, true)

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                console.log(xhr.responseText);
                let valid=true;
                console.log(xhr.responseText.length);
                for (let i=0; i<xhr.responseText.length; i++){
                    if (!"0123456789".includes(xhr.responseText[i])){
                        valid=false;
                        break;
                    }
                }
                if (valid){
                    window.location.href='/SAE_S3_war_exploded/double.jsp';
                }else{
                    console.log("Pas valide");
                }
            }
        }
    }
    xhr.send();

}

async function subscribe() {
    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let password = await sha256(document.getElementById("password").value).then(hash => {
        return hash
    });
    let confirm = await sha256(document.getElementById("confirm").value).then(hash => {
        return hash
    })
    if (email !== "" && password !== "" && password === confirm) {
        const xhr = new XMLHttpRequest()
        xhr.open('GET', `Connection?action=subscribe&username=${encodeURIComponent(name)}&email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`, true)
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    console.log(xhr.responseText)
                    if (xhr.responseText==="Abonnement réussi"){
                        window.location.href='/SAE_S3_war_exploded/login.jsp';
                    }
                }
            }
        }
        xhr.send();
    } else {
        console.error("Mot de passe non confirmé")
    }
}

async function sha256(password) {
    const msgBuffer = new TextEncoder().encode(password);
    const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    return hashArray.map(b => ('00' + b.toString(16)).slice(-2)).join('');
}

async function test() {
    let password = 'BonjourMaCouillasse';
    password = await sha256(password).then(hash => {
        return hash
    })
    console.log(password)
    console.log(password.length)
}

test()
test()


