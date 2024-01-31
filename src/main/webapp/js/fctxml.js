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
	var xhr = new XMLHttpRequest();
	xhr.open(verb, url);

	xhr.onload = function() {
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
		
	get("ServletCitation?nomauteur=" + encodeURIcomponent(selectedAuteur), xhr => {
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
	
	
	get("ServletGoogle?saisie=" + encodeURIcomponent(saisie), xhr => {
		let responseXML = xhr.responseXML;
		let propositions = responseXML.getElementsByTagName("proposition")
		
		resultZone.innerHTML = ""
		console.log(propositions)
		
		let listeUl = document.createElement("ul")
		
		Array.from(propositions)
			.forEach(element => {
				let proposition = element.innerHTML
				console.log(element)
				console.log(proposition)
				let elementLi = document.createElement("li")
				elementLi.innerHTML = proposition
				listeUl.appendChild(elementLi)
			
		})
		resultZone.appendChild(listeUl)
		resultZone.classList.remove("boite")
		
	})
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


});
