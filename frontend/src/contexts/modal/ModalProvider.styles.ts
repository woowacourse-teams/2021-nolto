import styled from 'styled-components';

import IconButton from 'components/@common/IconButton/IconButton';
import { PALETTE } from 'constants/palette';
import { Z_INDEX } from 'constants/styles';
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
`;

const CrossMarkButton = styled(IconButton)`
  position: absolute;
  top: -8px;
  right: -8px;
  background-color: ${PALETTE.PRIMARY_400};
`;

export default { ModalContainer, ModalInner, CrossMarkButton };
