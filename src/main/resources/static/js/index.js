// Alta clientes
document.getElementById("altaClientesForm").onsubmit = function(event) {
		event.preventDefault(); // Evita el envío predeterminado del formulario
		const dni = document.getElementById("dni").value;
		const nombre = document.getElementById("nombre").value;
		const apellido = document.getElementById("apellido").value;

		fetch(`/clientes/guardar/${dni}/${nombre}/${apellido}`)
		.then(async results => {
			if (!results.ok) {
				const message = await results.text();
				return alert(message)
			}

			const message = await results.text();
			alert(message)
			window.location.replace("clientes.html")
			
		})
	};

