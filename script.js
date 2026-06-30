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
        duration: 600,
        delay: index * 80,
        fill: 'forwards'
      }
    );
  });

  document.querySelectorAll('.filter-pill').forEach((pill) => {
    pill.addEventListener('click', () => {
      document.querySelectorAll('.filter-pill').forEach((item) => item.classList.remove('active'));
      pill.classList.add('active');
    });
  });
});
