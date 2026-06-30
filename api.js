const TMDB_API_KEY = 'c8ac72ad3535de0d68ee95a6bb28a544';
const TMDB_BASE_URL = 'https://api.themoviedb.org/3';
const FALLBACK_IMAGE = 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=900&q=80';
const INCEPTION_POSTER = 'https://image.tmdb.org/t/p/w500/xlaY2zyzMfkhk0HSC5VUwzoZPU1.jpg';
const GENRE_MAP = {
  28: 'Action',
  35: 'Comedy',
  18: 'Drama',
  53: 'Thriller',
  878: 'Sci-Fi',
  9648: 'Mystery',
  10749: 'Romance'
};

async function fetchMovies(endpoint, params = {}) {
  const url = new URL(`${TMDB_BASE_URL}${endpoint}`);
  url.searchParams.set('api_key', TMDB_API_KEY);
  Object.entries(params).forEach(([key, value]) => url.searchParams.set(key, value));
  const res = await fetch(url);
  if (!res.ok) throw new Error('Unable to load movie data');
  return res.json();
}

function getMovieGenres(movie) {
  const genres = (movie.genre_ids || []).slice(0, 2).map((id) => GENRE_MAP[id] || 'Featured');
  return genres.length ? genres.join(' • ') : 'Featured';
}

function buildMovieCard(movie) {
  const title = movie.title || movie.name || 'Untitled title';
  const isInception = title.toLowerCase() === 'inception';

  return {
    title,
    image: isInception ? INCEPTION_POSTER : movie.poster_path ? `https://image.tmdb.org/t/p/w500${movie.poster_path}` : FALLBACK_IMAGE,
    category: getMovieGenres(movie),
    rating: movie.vote_average?.toFixed(1) || 'N/A',
    overview: movie.overview || 'A cinematic selection from TMDb.'
  };
}

async function loadFeaturedMovies() {
  try {
    const data = await fetchMovies('/movie/popular', { language: 'en-US', page: 1 });
    const results = data.results || [];
    const curated = [
      { title: 'Inception', poster_path: '/oXUWEcUiONHk2MjM2r4dFJQK8ea.jpg', genre_ids: [878], vote_average: 8.8, overview: 'A skilled thief enters the dreams of others to steal secrets from their subconscious.' },
      ...results.filter((movie) => (movie.title || movie.name || '').toLowerCase() !== 'inception')
    ];
    return curated.slice(0, 6).map(buildMovieCard);
  } catch (error) {
    return [
      {
        title: 'Inception',
        image: INCEPTION_POSTER,
        category: 'Sci-Fi',
        rating: '8.8',
        overview: 'A skilled thief enters the dreams of others to steal secrets from their subconscious.'
      }
    ];
  }
}

async function renderMovies() {
  const cards = document.querySelectorAll('.movie-grid .movie-card');
  const featuredCards = document.querySelectorAll('.card-row .movie-card');
  const featured = await loadFeaturedMovies();

  const allCards = [...featuredCards, ...cards];
  const detailTitle = document.querySelector('.details-body h2');
  const detailText = document.getElementById('detail-summary');
  const detailMeta = document.getElementById('detail-meta-row');

  allCards.slice(0, featured.length).forEach((card, index) => {
    const movie = featured[index];
    const img = card.querySelector('img');
    const title = card.querySelector('h3');
    const meta = card.querySelector('p');

    if (img) {
      img.src = movie.image;
      img.onerror = () => {
        img.src = FALLBACK_IMAGE;
      };
    }
    if (title) title.textContent = movie.title;
    if (meta) meta.textContent = `${movie.category} • ${movie.rating}`;
  });

  if (detailTitle && featured[0]) {
    detailTitle.textContent = featured[0].title;
  }
  if (detailText && featured[0]) {
    detailText.textContent = featured[0].overview;
  }
  if (detailMeta && featured[0]) {
    detailMeta.innerHTML = `
      <span class="meta-chip">${new Date().getFullYear()}</span>
      <span class="meta-chip">${featured[0].category}</span>
      <span class="meta-chip">${featured[0].rating} IMDb</span>
    `;
  }
}

document.addEventListener('DOMContentLoaded', () => {
  renderMovies().catch(() => {
    console.warn('TMDb data could not be loaded.');
  });
});
