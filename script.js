document.addEventListener('DOMContentLoaded', () => {
  const footer = document.querySelector('.site-footer p');
  if (footer) {
    const year = new Date().getFullYear();
    footer.textContent = `Built for the Netflix-inspired project as a vivid multi-page experience — ${year}.`;
  }

  const cards = document.querySelectorAll('.movie-card, .content-section, .hero-panel');
  cards.forEach((card, index) => {
    card.animate(
      [
        { opacity: 0, transform: 'translateY(10px)' },
        { opacity: 1, transform: 'translateY(0)' }
      ],
      {
        duration: 650,
        delay: index * 70,
        fill: 'forwards'
      }
    );
  });

  const filterPills = document.querySelectorAll('.filter-pill');
  const movieCards = document.querySelectorAll('.movie-grid .movie-card');
  const searchInput = document.querySelector('.search-bar input');

  const updateMovieFilters = () => {
    const selectedFilter = document.querySelector('.filter-pill.active')?.dataset.filter || 'all';
    const query = searchInput?.value.trim().toLowerCase() || '';

    movieCards.forEach((card) => {
      const category = card.dataset.category || 'all';
      const title = card.querySelector('h3')?.textContent.toLowerCase() || '';
      const matchesSearch = !query || title.includes(query) || category.includes(query);
      const matchesFilter = selectedFilter === 'all' || category === selectedFilter;
      card.style.display = matchesSearch && matchesFilter ? '' : 'none';
    });
  };

  filterPills.forEach((pill) => {
    pill.addEventListener('click', () => {
      filterPills.forEach((item) => item.classList.remove('active'));
      pill.classList.add('active');
      updateMovieFilters();
    });
  });

  if (searchInput) {
    searchInput.addEventListener('input', updateMovieFilters);
  }

  // Carousel auto-shuffle for Now Playing
  const carouselTrack = document.getElementById('carouselTrack');
  const carouselTitle = document.getElementById('carouselTitle');
  const carouselDesc = document.getElementById('carouselDesc');
  const carouselTags = document.getElementById('carouselTags');

  const movies = [
    {
      title: 'Parasite',
      desc: 'A sharp social thriller about class conflict and deception.',
      tags: ['Thriller', 'Drama', 'Mystery']
    }
  ];

  let currentSlide = 0;

  const updateCarousel = () => {
    const offset = currentSlide * -100;
    carouselTrack.style.transform = `translateX(${offset}%)`;

    const movie = movies[currentSlide];
    carouselTitle.textContent = movie.title;
    carouselDesc.textContent = movie.desc;
    carouselTags.innerHTML = movie.tags.map(tag => `<span>${tag}</span>`).join('');
  };

  if (carouselTrack) {
    setInterval(() => {
      currentSlide = (currentSlide + 1) % movies.length;
      updateCarousel();
    }, 5000);
  }

  movieCards.forEach((card) => {
    card.addEventListener('mousemove', (event) => {
      const rect = card.getBoundingClientRect();
      const x = event.clientX - rect.left;
      const y = event.clientY - rect.top;
      const rotateY = ((x / rect.width) - 0.5) * 10;
      const rotateX = ((0.5 - (y / rect.height))) * 10;
      card.style.transform = `perspective(900px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) translateY(-8px) scale(1.01)`;
    });

    card.addEventListener('mouseleave', () => {
      card.style.transform = '';
    });
  });

  const carouselTrack = document.getElementById('carouselTrack');
  const carouselTitle = document.getElementById('carouselTitle');
  const carouselDesc = document.getElementById('carouselDesc');
  const carouselTags = document.getElementById('carouselTags');

  const movies = [
    {
      title: 'Inception',
      desc: 'A mind-bending heist thriller. Enter dreams to steal secrets.',
      tags: ['Sci-Fi', 'Action', 'Thriller']
    },
    {
      title: 'Parasite',
      desc: 'A sharp social thriller about class conflict and deception.',
      tags: ['Thriller', 'Drama', 'Mystery']
    },
    {
      title: 'The Matrix',
      desc: 'A cyberpunk classic. Reality is not what it seems.',
      tags: ['Sci-Fi', 'Action', 'Adventure']
    }
  ];

  let currentSlide = 0;

  const updateCarousel = () => {
    const offset = currentSlide * -100;
    carouselTrack.style.transform = `translateX(${offset}%)`;

    const movie = movies[currentSlide];
    carouselTitle.textContent = movie.title;
    carouselDesc.textContent = movie.desc;
    carouselTags.innerHTML = movie.tags.map(tag => `<span>${tag}</span>`).join('');
  };

  if (carouselTrack) {
    setInterval(() => {
      currentSlide = (currentSlide + 1) % movies.length;
      updateCarousel();
    }, 5000);
  }


    {
      title: 'Inception',
      blurb: 'The ultimate mind-bending blockbuster.'
    },
    {
      title: 'Parasite',
      blurb: 'A sharp, suspenseful social thriller.'
    },
    {
      title: 'The Matrix',
      blurb: 'A cyberpunk icon with timeless style.'
    }
  ];

  const heroTitle = document.querySelector('.hero-copy h1');
  const heroText = document.querySelector('.hero-copy .hero-text');

  if (heroTitle && heroText) {
    let currentSlide = 0;
    setInterval(() => {
      currentSlide = (currentSlide + 1) % heroSlides.length;
      heroTitle.textContent = heroSlides[currentSlide].title;
      heroText.textContent = heroSlides[currentSlide].blurb;
    }, 4000);
  }
});
