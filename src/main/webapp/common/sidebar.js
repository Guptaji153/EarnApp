window.addEventListener("load", function(){

    const toggleBtn =
    document.getElementById("menuToggle");

    const overlay =
    document.getElementById("overlay");

    const sidebar =
    document.querySelector(".sidebar");

    if(toggleBtn && overlay && sidebar){

        toggleBtn.addEventListener(
        "click",
        function(){

            sidebar.classList.toggle("open");

            if(sidebar.classList.contains("open")){

                overlay.style.display = "block";

                toggleBtn.innerHTML = "✕";

            } else {

                overlay.style.display = "none";

                toggleBtn.innerHTML = "☰";
            }
        });

        overlay.addEventListener(
        "click",
        function(){

            sidebar.classList.remove("open");

            overlay.style.display = "none";

            toggleBtn.innerHTML = "☰";
        });

        const menuLinks =
        document.querySelectorAll(
        ".menu a, .menu .menu-link");

        menuLinks.forEach(function(link){

            link.addEventListener(
            "click",
            function(){

                if(window.innerWidth <= 768){

                    sidebar.classList.remove("open");

                    overlay.style.display = "none";

                    toggleBtn.innerHTML = "☰";
                }
            });
        });

        window.addEventListener(
        "resize",
        function(){

            if(window.innerWidth > 768){

                sidebar.classList.remove("open");

                overlay.style.display = "none";

                toggleBtn.innerHTML = "☰";
            }
        });
    }

});