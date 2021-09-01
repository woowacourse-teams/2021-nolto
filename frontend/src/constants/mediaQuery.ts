export const BREAK_POINTS = {
  MOBILE: '375px',
  TABLET: '768px',
  DESKTOP_SMALL: '960px',
  DESKTOP: '1024px',
};

export const MEDIA_QUERY = {
  MOBILE: `screen and (max-width: ${BREAK_POINTS.MOBILE})`,
  TABLET: `screen and (max-width: ${BREAK_POINTS.TABLET})`,
  DESKTOP_SMALL: `screen and (max-width: ${BREAK_POINTS.DESKTOP_SMALL})`,
  DESKTOP: `screen and (max-width: ${BREAK_POINTS.DESKTOP})`,
};
