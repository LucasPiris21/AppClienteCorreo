document.addEventListener("DOMContentLoaded", () => {
  const splash = document.getElementById("splash");
  const content = document.getElementById("content");

  // Espera 2 segundos antes de desaparecer el splash
  setTimeout(() => {
    splash.style.opacity = "0";
    splash.style.visibility = "hidden";
    content.classList.remove("hidden");
    content.classList.add("show");
  }, 2000);
});
