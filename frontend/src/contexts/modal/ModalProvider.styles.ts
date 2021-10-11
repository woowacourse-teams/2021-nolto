import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { Z_INDEX } from 'constants/styles';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { defaultShadow } from 'commonStyles';

const ModalContainer = styled.div`
  position: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: ${Z_INDEX.MODAL};
`;

const ModalInner = styled.div`
  position: relative;
  display: inline-block;
  background-color: ${PALETTE.WHITE_400};
  border-radius: 25px;
  padding: 4.75rem;
  ${defaultShadow};

  @media ${MEDIA_QUERY.MOBILE} {
    padding: 2rem;
  }
`;

const CrossMarkButton = styled.button`
  width: 2rem;
  height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  top: -8px;
  right: -8px;
  border: none;
  border-radius: 50%;
  background-color: ${PALETTE.PRIMARY_400};
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
`;

export default { ModalContainer, ModalInner, CrossMarkButton };
