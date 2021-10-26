import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { Z_INDEX } from 'constants/styles';

const Root = styled.div`
  z-index: ${Z_INDEX.DROPDOWN};

  @media ${MEDIA_QUERY.TABLET} {
    & .modify-button {
      display: none;
    }
  }

  & .delete-button {
    color: ${PALETTE.RED_400};
  }
`;

export default { Root };
