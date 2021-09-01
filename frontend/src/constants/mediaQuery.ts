export const BREAK_POINTS = {
  MOBILE: '375px',
  TABLET: '768px',
  DESKTOP: '1024px',
};

export const MEDIA_QUERY = {
  MOBILE: `screen and (max-width: ${BREAK_POINTS.MOBILE})`,
  TABLET: `screen and (max-width: ${BREAK_POINTS.TABLET})`,
  DESKTOP: `screen and (max-width: ${BREAK_POINTS.DESKTOP})`,
};
