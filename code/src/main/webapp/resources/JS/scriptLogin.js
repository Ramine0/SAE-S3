let user

async function login(){
    let email=document.getElementById('email').value
    let password=document.getElementById('password').value
    password = await sha256(password).then(hash => {return hash})
    const xhr=new XMLHttpRequest()
    xhr.open('GET',`Connection?action=connect&email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`, true)

    xhr.onreadystatechange = function() {
        if (xhr.readyState===XMLHttpRequest.DONE){
            if (xhr.status===200){
                user=xhr.responseText
                console.log(xhr.responseText);
            }
        }
    }
    xhr.send();

}

async function subscribe(){
    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let password = await sha256(document.getElementById("password").value).then(hash => {return hash} );
    let confirm= await sha256(document.getElementById("confirm").value).then(hash => {return hash})
    if (email!=="" && password!=="" && password===confirm){
        console.log('confirme');
        const xhr=new XMLHttpRequest()
        xhr.open('GET', `Connection?action=${encodeURIComponent('subscribe')}&username=${encodeURIComponent(name)}&email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`, true)
        xhr.onreadystatechange = function(){
            if (xhr.readyState===XMLHttpRequest.DONE){
                if (xhr.status===200){
                    console.log(xhr.responseText)
                }
            }
        }
        xhr.send();
    }else{
        console.error("Mot de passe non confirmé")
    }
}

async function sha256(password){
    const msgBuffer = new TextEncoder().encode(password);
    const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);
    const hashArray=Array.from(new Uint8Array(hashBuffer));
    return hashArray.map(b => ('00' +b.toString(16)).slice(-2)).join('');
}

async function test(){
    let password='BonjourMaCouillasse';
    password = await sha256(password).then(hash => {return hash})
    console.log(password)
    console.log(password.length)
}

test()
test()

function initPlacements(){
    const mapList=document.getElementById("planType")
    const init=new XMLHttpRequest()
    init.open('POST', 'Connection?action=init')
    init.onreadystatechange = function () {
        if (xhr.readyState===XMLHttpRequest.DONE){
            if (xhr.status===200){
                const placements=xhr.responseText.split(";")
                for (let i=0; i<placements.length; i++){
                    const info=placements[i].split(",")
                    let p=document.createElement("option");
                    p.value=`${info[0]}${info[1]}`
                    p.text=info[1]
                    mapList.appendChild(p)
                }
            }
        }
    }
    init.send()
}