/**
 * Cette méthode "Ajax" affiche le XML.
 *
 * On utilise la propriété 'responseText' de l'objet XMLHttpRequest afin
 * de récupérer sous forme de texte le flux envoyé par le serveur.
 */
function afficheXML() {
	// Objet XMLHttpRequest.
	var xhr = new XMLHttpRequest();

	// Requête au serveur avec les paramètres éventuels.
	xhr.open("GET", "ServletAuteur");

	// On précise ce que l'on va faire quand on aura reçu la réponse du serveur.
	xhr.onload = function () {
		// Si la requête http s'est bien passée.
		if (xhr.status === 200) {
			// Elément html que l'on va mettre à jour.
			var elt = document.getElementById("tt_zone");
			elt.innerHTML = xhr.responseText;
		}
	};

	// Envoie de la requête.
	xhr.send();
}

function get(url, onSuccess) {
	ajax("GET", url, onSuccess);
}

function post(url, onSuccess) {
	ajax("POST", url, onSuccess);
	
}

function ajax(verb, url, onSuccess, onFail = null) {
	// Objet XMLHttpRequest.
	let xhr = new XMLHttpRequest();

	url = "http://localhost:8080/TpAjaxEtd/" + url;

	xhr.open(verb, url);
	if ("POST" == verb) {
        xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	}

	xhr.onload = () => {
		if (xhr.status === 200) {
			onSuccess(xhr);
		}
	};
	xhr.send();
}


/**
 * Cette méthode "Ajax" affiche la liste des auteurs.
 */
function l_auteurs() {
	let listeAuteurs = document.getElementById("lnom");

	get("ServletAuteur", (xhr) => {
		let responseXML = xhr.responseXML;
		let nuplets = responseXML.getElementsByTagName("nuplet");
		
		listeAuteurs.innerHTML = ""
		
		if (nuplets.length <= 0) {
			const option = document.createElement("option")
			option.text = "------"
			listeAuteurs.add(option)
			
		} else {
			Array.from(nuplets)
				.forEach(element => {
					let id = element.getAttribute("id")
					let nom = element.getElementsByTagName("nom")[0].innerHTML
	
					const option = document.createElement("option")
					option.value = id
					option.text = nom
					listeAuteurs.add(option)
				});
			
		}
	})
}


/**
 * Cette méthode "Ajax" affiche la liste des citations.
 */
function l_citations() {
	let divCitations = document.getElementById("lcitations");
	let listeAuteurs = document.getElementById("lnom");
	
	divCitations.innerHTML = ""
	
	let selectedAuteur = listeAuteurs.options[listeAuteurs.selectedIndex].text;
		
	get("ServletCitation?nomauteur=" + encodeURIComponent(selectedAuteur), xhr => {
		let responseXML = xhr.responseXML;
		let citations = responseXML.getElementsByTagName("citation")
		
		let listeUl = document.createElement("ul")
		
		Array.from(citations).forEach(element => {
			let citation = element.innerHTML
			
			let elementLi = document.createElement("li")
			elementLi.innerHTML = citation
			listeUl.appendChild(elementLi)
			
		})
		divCitations.appendChild(listeUl)
	})
}


/**
 * Cette méthode "Ajax" simule la zone de recherche 'Google'.
 */
function processKey() {
	
	let fieldGoogle = document.getElementById("saisie")
	let saisie = fieldGoogle.value;
	let resultZone = document.getElementById("zoneaff")
	
	if (saisie.length == 0) {
		resultZone.innerHTML = ""
	}
	
	
	get("ServletGoogle?saisie=" + encodeURIComponent(saisie), xhr => {
		let responseXML = xhr.responseXML;
		let propositions = responseXML.getElementsByTagName("proposition")
		
		resultZone.innerHTML = ""
		
		let listeUl = document.createElement("ul")
		
		Array.from(propositions)
			.forEach(element => {
				let proposition = element.firstChild.nodeValue
				let elementLi = document.createElement("li")

				let link = document.createElement("a")
				link.setAttribute("href", "https://www.google.com/search?q=" + encodeURIComponent(proposition))

				link.innerHTML = proposition
				elementLi.addEventListener("contextmenu", ev => {
					ev.preventDefault()
					deleteMot(proposition)
					return false
				})

				elementLi.appendChild(link)
				listeUl.appendChild(elementLi)
			
		})
		resultZone.appendChild(listeUl)
		resultZone.style.display = "block"
		
	})
}

function deleteMot(mot) {
	if (mot == null || mot.length == 0) {
		return
	}

	console.log(mot)

	post("ServletDelete?mot=" + encodeURI(mot), xhr => {
		let message = getMessage(xhr)

		let status = getStatus(xhr)

		if (status != 200) {
			alert(message)
		} else {
			alert(message)

			let searchedInput = findInputByValue(mot)
			if (searchedInput != null) {
				let parentDiv = searchedInput.parentElement
				parentDiv.remove()
			}
		}

		processKey()

	})
}

function findInputByValue(value) {
	let inputs = document.querySelectorAll("#formAdd div input:not([type=button])");
	let element = null

	if (inputs != null && inputs.length > 0) {
		let inputsFiltered = Array.from(inputs)
			.filter(el => el.value == value)
		element = inputsFiltered.length == 0
			? null
			: inputsFiltered[0]
	}

	return element
}


/**
 * Cette méthode "Ajax" permet de tester les paramètres passés par l'url.
 */
function testEncodeUrl() {
	// Objet XMLHttpRequest.
	var xhr = new XMLHttpRequest();

	// Requête au serveur avec les paramètres éventuels.
	var param = "texte=" + encodeURIComponent(document.getElementById("envoie").value);
	var url = "ServletEncode";
	alert(url + "?" + param);

	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	// On précise ce que l'on va faire quand on aura reçu la réponse du serveur.
	xhr.onload = function () {
		// Si la requête http s'est bien passée.
		if (xhr.status === 200)
			// Elément html que l'on va mettre à jour.
			document.getElementById("recue").value = xhr.responseXML.getElementsByTagName("msg")[0].firstChild.nodeValue;
	};

	// Envoie de la requête.
	xhr.send(param);
}

function computeExists(event) {
	const srcElement = event.srcElement;
	let value = srcElement.value;

	let button = document.querySelector("#formAdd div input[type='button']");

	let nextSpan = srcElement.nextSibling

	if (nextSpan == null) {
		let newSpan = document.createElement("span")
		srcElement.insertAdjacentElement("afterend", newSpan)
		nextSpan = newSpan
	}

	if (value.length == 0) {
		button.setAttribute("disabled", "disabled")
		nextSpan.innerHTML = ""
		return;
	}

	get("ServletAdd?saisie=" + encodeURIComponent(value), xhr => {
		let message = xhr.responseXML.getElementsByTagName("message")[0]
		let messageStatus = message.getAttribute("statut")
		let messageText = message.innerHTML

		if (messageStatus == 200) {
			button.removeAttribute("disabled")
		} else {
			button.setAttribute("disabled", "disabled")
			
		}
		nextSpan.innerHTML = messageText

	})
}

function addMessage() {
	let inputs = document.querySelectorAll("#formAdd div input[type='text']");
	let inputTyped = inputs[inputs.length - 1]

	let value = inputTyped.value
	
	let nextSpan = inputTyped.nextSibling

	let button = document.querySelector("#formAdd div input[type='button']");
	if (nextSpan == null) {
		let newSpan = document.createElement("span")
		srcElement.insertAdjacentElement("afterend", newSpan)
		nextSpan = newSpan
	}

	if (value.length == 0) {
		button.setAttribute("disabled", "disabled")
		nextSpan.innerHTML = ""
		return;
	}

	post("ServletAdd?saisie=" + encodeURIComponent(value), xhr => {
		let message = getMessage(xhr)

		let status = getStatus(xhr)

		if (status != 200) {
			nextSpan.innerHTML = message
		} else {

			let newSpan = document.createElement("span")
			newSpan.innerHTML = message
	
			let div = document.createElement("div")
			let newInput = document.createElement("input")
			newInput.disabled = true
			newInput.value = value
			div.appendChild(newInput)
			div.appendChild(newSpan)
	
			inputTyped.insertAdjacentElement("beforebegin", div)
			inputTyped.value = ""
			nextSpan.innerHTML = ""
			inputTyped.focus()
			processKey()

		}
	})
}

function getMessage(xhr) {
	let elementNode = getTagElement(xhr, "message");

	return elementNode == null
		? null
		: elementNode.innerHTML;
}

function getStatus(xhr) {
	let elementNode = getTagElement(xhr, "status");

	return elementNode == null
		? null
		: elementNode.innerHTML;
}

function getTagElement(xhr, tagName) {

	let elements = xhr.responseXML
		.getElementsByTagName(tagName)

	return elements == null
		? null
		: elements[0]
}


/**
 * Lancement après le chargement du DOM.
 */
document.addEventListener("DOMContentLoaded", () => {

	document.getElementById("bt_zone")
		.addEventListener("click", afficheXML);

	document.getElementById("bt_Url")
		.addEventListener("click", testEncodeUrl);

	document.getElementById("bt_auteurs")
		.addEventListener("click", l_auteurs)
		
	document.getElementById("lnom")
		.addEventListener("change", l_citations)
		
	document.getElementById("saisie")
		.addEventListener("input", processKey)

	document.querySelector("#formAdd div input[type='text']")
		.addEventListener("keyup", computeExists)

	document.querySelector("#formAdd div input[type='button']")
		.addEventListener("click", addMessage)

});
