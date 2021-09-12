import styled from 'styled-components';

import IconButtonComponent from 'components/@common/IconButton/IconButton';
import ArrowIcon from 'assets/carouselArrow.svg';

const ArrowUp = styled(ArrowIcon)`
  transform: rotate(-90deg);
`;

export const ScrollUpButton = styled(IconButtonComponent)`
  width: 2.25rem;
  height: 2.25rem;
  padding: 0.55rem;
  position: fixed;
  right: 1rem;
  bottom: 1rem;
`;

export default { ArrowUp };
