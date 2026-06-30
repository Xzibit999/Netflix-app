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

  filterPills.forEach((pill) => {
    pill.addEventListener('click', () => {
      const selectedFilter = pill.dataset.filter || 'all';

      filterPills.forEach((item) => item.classList.remove('active'));
      pill.classList.add('active');

      movieCards.forEach((card) => {
        const category = card.dataset.category || 'all';
        const shouldShow = selectedFilter === 'all' || category === selectedFilter;
        card.style.display = shouldShow ? '' : 'none';
      });
    });
  });

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

  const heroSlides = [
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
